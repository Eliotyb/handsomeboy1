package com.community.groupon.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String username;
    private String name;
    private Integer role;
}
