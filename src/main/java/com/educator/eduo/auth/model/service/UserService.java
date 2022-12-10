package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

public interface UserService {

    JwtResponse authenticate(LoginDto loginDto);

    JwtResponse getUserInfoUsingKakao(String accessTokenByKakao) throws JsonProcessingException;

    int registerUser(Map<String, Object> params);

}
