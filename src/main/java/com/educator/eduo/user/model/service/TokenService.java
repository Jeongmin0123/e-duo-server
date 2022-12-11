package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.JwtResponse;

public interface TokenService {

    JwtResponse reissueAccessToken(String refreshToken);
}
