package com.educator.eduo.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {

    private String userId;
    private String domain;
    private String password;

    public Map<String, String> getEmail() {
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("userId", userId);
        emailMap.put("domain", domain);
        return emailMap;
    }
}
