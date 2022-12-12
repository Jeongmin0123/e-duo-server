package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.JwtResponse;
import com.educator.eduo.user.model.dto.LoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService {

    JwtResponse authenticate(LoginDto loginDto);

    Object getUserInfoUsingKakao(String accessTokenByKakao) throws JsonProcessingException;

    Optional<JwtResponse> registerUser(Map<String, Object> params) throws IllegalArgumentException, UsernameNotFoundException;

    boolean isExistsUserId(String userId);

}
