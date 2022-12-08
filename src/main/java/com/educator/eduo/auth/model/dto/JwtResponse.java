package com.educator.eduo.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String userId;
    private String name;
    private String role;

}
