package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.dto.OAuthLoginDto;

public interface UserService {
    JwtResponse authenticate(LoginDto loginDto);


    JwtResponse oauthLogin(OAuthLoginDto oAuthLoginDto);
}
