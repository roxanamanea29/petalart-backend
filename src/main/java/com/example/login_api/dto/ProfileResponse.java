package com.example.login_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}