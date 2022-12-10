package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;

import java.util.Map;

public interface UserService {
    JwtResponse authenticate(LoginDto loginDto);


    JwtResponse oauthLogin(LoginDto loginDto);

    int registerUser(Map<String, Object> params);
}
