package com.example.login_api.dto;

import lombok.Data;

@Data
public class AddressRequest {
    private Long userId;
    private String street;
    private String streetNumber;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String addressType;
}
