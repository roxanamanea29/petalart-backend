package com.example.login_api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
    private String password;
}
