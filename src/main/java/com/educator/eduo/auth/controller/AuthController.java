package com.educator.eduo.auth.controller;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.service.TokenService;
import com.educator.eduo.auth.model.service.UserService;
import com.educator.eduo.security.TokenProvider;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthController(TokenService tokenService, UserService userService, TokenProvider tokenProvider) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
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
        JwtResponse jwtResponse = userService.getUserInfoUsingKakao(accessTokenByKakao);
        return ResponseEntity.ok(jwtResponse);
    }
}
