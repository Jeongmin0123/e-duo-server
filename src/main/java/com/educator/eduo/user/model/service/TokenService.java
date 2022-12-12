package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.JwtResponse;
import java.sql.SQLException;

public interface TokenService {

    JwtResponse reissueAccessToken(String refreshToken) throws SQLException;

}
