package com.example.login_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private String fullName;
    private String email;
    private String role;
}