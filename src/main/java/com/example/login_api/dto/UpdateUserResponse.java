package com.example.login_api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;



}
