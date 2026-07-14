package com.playmate.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String role;
    private Object userInfo;

    public LoginResponse(String token, String role, Object userInfo) {
        this.token = token;
        this.role = role;
        this.userInfo = userInfo;
    }
}
