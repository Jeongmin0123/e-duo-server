package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.JwtResponse;
import com.educator.eduo.user.model.dto.LoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService {

    JwtResponse authenticate(LoginDto loginDto) throws SQLException;

    Object getUserInfoUsingKakao(String accessTokenByKakao) throws SQLException, JsonProcessingException;

    JwtResponse registerUser(Map<String, Object> params)
            throws SQLException, DuplicateKeyException, IllegalArgumentException, UsernameNotFoundException;

    boolean isExistsUserId(String userId) throws SQLException;

}
