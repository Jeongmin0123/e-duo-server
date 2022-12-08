package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.entity.Token;
import com.educator.eduo.auth.model.mapper.TokenMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenMapper tokenMapper;

    @Autowired
    public TokenServiceImpl(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Token> findTokenByUserId(String userId) {
        return tokenMapper.selectTokenByUserId(userId);
    }

    @Override
    public Optional<Token> findTokenByRefreshToken(String refreshToken) {
        return tokenMapper.selectTokenByRefreshToken(refreshToken);
    }

    @Override
    @Transactional
    public boolean registerToken(Token token) {
        return tokenMapper.insertToken(token) == 1;
    }

    @Override
    public boolean updateTokenByUserId(Token token) {
        return tokenMapper.updateTokenByUserId(token) == 1;
    }

    @Override
    public boolean deleteTokenByUserId(String userId) {
        return tokenMapper.deleteTokenByUserId(userId) == 1;
    }

}
