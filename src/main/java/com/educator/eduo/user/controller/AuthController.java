package com.educator.eduo.user.controller;

import com.educator.eduo.user.model.dto.AuthMailDto;
import com.educator.eduo.user.model.dto.JwtResponse;
import com.educator.eduo.user.model.dto.LoginDto;
import com.educator.eduo.user.model.entity.User;
import com.educator.eduo.user.model.service.MailService;
import com.educator.eduo.user.model.service.TokenService;
import com.educator.eduo.user.model.service.AuthService;
import com.educator.eduo.security.TokenProvider;
import com.educator.eduo.user.model.service.UserService;
import com.educator.eduo.util.NumberGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final AuthService authService;
    private final MailService mailService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthController(TokenService tokenService, AuthService authService, MailService mailService, UserService userService,
            TokenProvider tokenProvider) {
        this.tokenService = tokenService;
        this.authService = authService;
        this.mailService = mailService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto) {
        JwtResponse jwtResponse = authService.authenticate(loginDto);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/api/refreshtoken")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request) {
        String refreshToken = tokenProvider.resolveToken(request);
        JwtResponse jwtResponse = tokenService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout() {
        logger.info("[LOGOUT] userId: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth/oauthlogin")
    public ResponseEntity<?> kakaoLogin(HttpServletRequest request) throws JsonProcessingException {
        String accessTokenByKakao = tokenProvider.resolveToken(request);
        Object userInfo = authService.getUserInfoUsingKakao(accessTokenByKakao);

        if (userInfo instanceof JwtResponse) {
            return ResponseEntity.ok(userInfo);
        }

        if (userInfo instanceof LoginDto) {
            return new ResponseEntity<>(userInfo, HttpStatus.BAD_REQUEST);
        }

        throw new RuntimeException("이 코드는 실행될 수 없습니다.");
    }

    @PostMapping("/auth/email")
    public ResponseEntity<?> emailValidCheck(@Value("${spring.mail.username}") String from, @RequestBody String userId) throws MessagingException {
        logger.info("Our Domain : {} to User : {}", from, userId);

        if (!authService.isExistsUserId(userId)) {
            String emailAuthCode = NumberGenerator.generateRandomUniqueNumber(6);
            AuthMailDto mailDto = AuthMailDto.builder()
                                             .from(from)
                                             .to(userId)
                                             .subject("[Eduo] 회원가입 이메일 인증 코드입니다.")
                                             .build()
                                             .setContentForAuthCode(emailAuthCode);

            mailService.sendEmailAuthCode(mailDto);
            return new ResponseEntity<>(emailAuthCode, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, Object> params) throws IllegalArgumentException {
        Optional<JwtResponse> result = authService.registerUser(params);
        logger.info("result to register user: {}", result);

        if (!result.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(result.get());
    }

    @PostMapping("/auth/mypw")
    public ResponseEntity<?> findPassword(@Value("${spring.mail.username}") String from, @RequestBody String userId) throws UsernameNotFoundException, MessagingException {
        String uuidPassword = UUID.randomUUID().toString().substring(18);
        User user = User.builder()
                        .userId(userId)
                        .password(uuidPassword)
                        .build();
        userService.updatePassword(user);

        AuthMailDto mailDto = AuthMailDto.builder()
                                         .from(from)
                                         .to(userId)
                                         .subject("[Eduo] 임시 비밀번호가 발급되었습니다.")
                                         .build()
                                         .setContentForPassword(uuidPassword);
        mailService.sendEmailAuthCode(mailDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
