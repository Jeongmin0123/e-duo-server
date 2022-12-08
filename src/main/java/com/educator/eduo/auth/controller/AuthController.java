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
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

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
        logger.info("Login Target: {}", loginDto);
        Authentication authentication = saveAuthentication(loginDto);
        Token newToken = registerOrUpdateJwtToken(authentication);
        return ResponseEntity.ok(createJwtResponse(authentication, newToken));
    }

    @PostMapping("/api/requesttoken")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request) throws AuthenticationException {
        String refreshToken = tokenProvider.resolveToken(request);

        if (refreshToken == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Token token = tokenService.findTokenByRefreshToken(refreshToken)
                                  .orElseThrow(() -> new IllegalArgumentException("토큰을 DB에서 찾을 수 없습니다."));

        if (!tokenProvider.validateToken(refreshToken)) {
            tokenService.deleteTokenByUserId(token.getUserId());
            throw new AuthenticationException("만료된 토큰입니다.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = tokenProvider.createAccessToken(authentication);
        token.setAccessToken(newAccessToken);

        tokenService.updateTokenByUserId(token);
        return ResponseEntity.ok(createJwtResponse(authentication, token));
    }

    @GetMapping("/api/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Authentication saveAuthentication(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUserId(), loginDto.getPassword()
        );

        // 메서드 authenticate()에서 UserDetailsServiceImpl의 loadUserByUserName을 호출하고, 최종적으로 Authentication을 만들어준다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Save Authenticate In Context: {}", authentication);

        return authentication;
    }

    private Token registerOrUpdateJwtToken(Authentication authentication) {
        Optional<Token> oldToken = tokenService.findTokenByUserId(authentication.getName());
        Token newToken = tokenProvider.createNewToken(authentication);

        if (oldToken.isPresent()) {
            tokenService.updateTokenByUserId(newToken);
        } else {
            tokenService.registerToken(newToken);
        }

        return newToken;
    }

    private JwtResponse createJwtResponse(Authentication authentication, Token newToken) {
        User user = (User) authentication.getPrincipal();
        List<String> roles = getRoles(user);

        return JwtResponse.builder()
                          .token(newToken)
                          .userId(user.getUserId())
                          .name(user.getName())
                          .roles(roles)
                          .build();
    }

    private List<String> getRoles(User user) {
        return user.getAuthorities()
                   .stream()
                   .map(GrantedAuthority::getAuthority)
                   .collect(Collectors.toList());
    }

}
