package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.dto.OAuthLoginDto;

import java.util.Map;

public interface UserService {
    JwtResponse authenticate(LoginDto loginDto);


    JwtResponse oauthLogin(OAuthLoginDto oAuthLoginDto);

    int registerUser(Map<String, Object> params);
}
