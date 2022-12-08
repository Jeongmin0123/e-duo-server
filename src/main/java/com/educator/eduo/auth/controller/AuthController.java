package com.educator.eduo.auth.controller;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.entity.Token;
import com.educator.eduo.auth.model.entity.User;
import com.educator.eduo.auth.model.service.TokenService;
import com.educator.eduo.auth.model.service.UserService;
import com.educator.eduo.security.TokenProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider,
            TokenService tokenService, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto) {
        JwtResponse jwtResponse = userService.authenticate(loginDto);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/api/refreshtoken")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request) {
        String refreshToken = tokenProvider.resolveToken(request);
        JwtResponse jwtResponse = userService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
