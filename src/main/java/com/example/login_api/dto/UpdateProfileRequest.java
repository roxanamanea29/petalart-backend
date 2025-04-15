package com.example.login_api.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String phone;
    }


