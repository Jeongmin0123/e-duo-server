package com.educator.eduo.auth.controller;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.dto.OAuthLoginDto;
import com.educator.eduo.auth.model.service.TokenService;
import com.educator.eduo.auth.model.service.UserService;
import com.educator.eduo.security.TokenProvider;

import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin("*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    public AuthController(TokenProvider tokenProvider, TokenService tokenService, UserService userService, PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto) {
        JwtResponse jwtResponse = userService.authenticate(loginDto);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/api/refreshtoken")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request) {
        String refreshToken = tokenProvider.resolveToken(request);
        JwtResponse jwtResponse = tokenService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth/oauthlogin")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> access) throws Exception{
        String accessTokenByKakao = access.get("accessToken");

        // header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessTokenByKakao);
        // HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000); // 읽기시간초과, ms
        factory.setConnectTimeout(3000); // 연결시간초과, ms
        HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(100) // connection pool 적용
                .setMaxConnPerRoute(5) // connection pool 적용
                .build();
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅
        RestTemplate restTemplate = new RestTemplate(factory);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET, request,
                String.class);
        logger.info("Kakao랑 서버 통신 이후 응답 : {}", response);
        logger.info("response.getBody() = {}", response.getBody());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode newNode = mapper.readTree(response.getBody());
        logger.info("JsonNode : {}", newNode);
        ObjectNode node = ((ObjectNode) newNode).put("Authentication", "Successful");
        logger.info("ObjectNode : {}", node);
//            String name = new ObjectMapper().writeValueAsString(node.get("properties").get("nickname"));
        String email = new ObjectMapper().writeValueAsString(node.get("kakao_account").get("email"));
        logger.info("email : {}", email); // oasdbd@laskdn.consal
        String[] temp = email.trim().split("@");
        String userId = temp[0];
        String domain = temp[1];
        String uuid = UUID.randomUUID().toString();
        OAuthLoginDto oAuthLoginDto = OAuthLoginDto.builder()
                .userId(userId)
                .password(passwordEncoder.encode(uuid))
                .domain(domain)
                .build();
        JwtResponse jwtResponse = userService.oauthLogin(oAuthLoginDto);
        if(jwtResponse == null) return new ResponseEntity<>(oAuthLoginDto, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, Object> params) {
        int result = userService.registerUser(params);
        logger.info("register user result : {}", result);
        if(result == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (result == -1) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
