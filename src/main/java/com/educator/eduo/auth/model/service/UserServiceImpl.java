package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.dto.OAuthLoginDto;
import com.educator.eduo.auth.model.entity.Token;
import com.educator.eduo.auth.model.entity.User;
import com.educator.eduo.auth.model.mapper.TokenMapper;
import com.educator.eduo.auth.model.mapper.UserMapper;
import com.educator.eduo.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;


    @Override
    @Transactional
    public JwtResponse authenticate(LoginDto loginDto) {
        logger.info("Login Target: {}", loginDto);
        Authentication authentication = saveAuthentication(loginDto);
        Token newToken = registerOrUpdateJwtToken(authentication);
        return tokenProvider.createJwtResponse(authentication, newToken);
    }

    @Override
    @Transactional
    public JwtResponse oauthLogin(OAuthLoginDto oAuthLoginDto) {
        User user = userMapper.selectUserByEmail(oAuthLoginDto.getEmail()).orElse(null);
        if(user != null) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    oAuthLoginDto.getUserId(), "", user.getAuthorities()
            );
            Token newToken = registerOrUpdateJwtToken(authentication);
            return tokenProvider.createJwtResponse(authentication, newToken);
        }
        return null;
    }

    @Override
    public int registerUser(Map<String, Object> params) {
        // 1. userId@domain 으로 아이디 중복 검사
        Map<String, String> userMap = new HashMap<>();
        userMap.put("userId", (String) params.get("userId"));
        userMap.put("domain", (String) params.get("domain"));
        if(userMapper.selectUserByUserId(userMap) != 0) return -1;

        // 2. ObjectMapper -> 맞는 VO로 변환 후 user 테이블과 role에 맞는 테이블에 정보 입력
        return 0;
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
        Optional<Token> oldToken = tokenMapper.selectTokenByUserId(authentication.getName());
        Token newToken = tokenProvider.createNewToken(authentication);

        if (oldToken.isPresent()) {
            tokenMapper.updateTokenByUserId(newToken);
        } else {
            tokenMapper.insertToken(newToken);
        }

        return newToken;
    }

}
