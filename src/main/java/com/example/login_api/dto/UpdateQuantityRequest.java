package com.example.login_api.dto;


import lombok.Data;

@Data
public class UpdateQuantityRequest {
    private Long productId;
    private int quantity;
}
