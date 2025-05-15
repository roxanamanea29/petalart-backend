package com.example.login_api.dto;


import com.example.login_api.enums.AddressType;
import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String street;
    private String streetNumber;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private AddressType addressType;

}
