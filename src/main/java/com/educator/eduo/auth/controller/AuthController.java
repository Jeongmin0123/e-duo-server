package com.educator.eduo.auth.controller;

import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.service.TokenService;
import com.educator.eduo.auth.model.service.UserService;
import com.educator.eduo.security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(TokenProvider tokenProvider, TokenService tokenService,
                          AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto) {
        logger.info("Login 시도", loginDto);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUserId(), loginDto.getPassword()
        );

        // authenticate -> UserDetailsService 의 loadByUsername이 실행 -> 권한까지 가지고 옴
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        logger.info("authenticate => {}", authentication);


        return ResponseEntity.ok("WWW");
    }
}
