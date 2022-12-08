package com.educator.eduo.auth.model.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {

    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String name;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, String userId, String name, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.name = name;
        this.roles = roles;
    }

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
