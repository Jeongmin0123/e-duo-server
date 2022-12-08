package com.educator.eduo.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {

    private static final String TYPE = "Bearer";
    
    private String accessToken;
    private String refreshToken;
    private String userId;
    private List<String> roles;
    private String name;

    public JwtResponse(String accessToken, String refreshToken, String userId, List<String> roles, String name) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.roles = roles;
        this.name = name;
    }

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
