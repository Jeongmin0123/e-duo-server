package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.entity.Token;
import java.util.Optional;

public interface TokenService {

    JwtResponse reissueAccessToken(String refreshToken);
}
