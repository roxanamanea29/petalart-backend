package com.example.login_api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductRequest {
    private Long productId;
    private int quantity;
}
