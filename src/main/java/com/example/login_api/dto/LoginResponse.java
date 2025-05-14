package com.example.login_api.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginResponse {

    private final String accessToken;
    private final List<String> roles;
    private Long userId;
    private String email;
    private String name;

}
