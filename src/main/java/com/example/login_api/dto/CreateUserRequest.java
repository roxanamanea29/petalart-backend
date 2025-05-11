package com.example.login_api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
    private String password;
}
