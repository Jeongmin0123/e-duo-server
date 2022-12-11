package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.JwtResponse;
import com.educator.eduo.user.model.entity.Token;
import com.educator.eduo.user.model.mapper.TokenMapper;

import com.educator.eduo.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;
    private final TokenMapper tokenMapper;


    @Autowired
    public TokenServiceImpl(TokenProvider tokenProvider, TokenMapper tokenMapper) {
        this.tokenProvider = tokenProvider;
        this.tokenMapper = tokenMapper;
    }

    @Override
    @Transactional
    public JwtResponse reissueAccessToken(String refreshToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = tokenProvider.createAccessToken(authentication);
        Token token = new Token(authentication.getName(), newAccessToken, refreshToken);

        tokenMapper.updateTokenByUserId(token);
        return tokenProvider.createJwtResponse(authentication, token);
    }

}
