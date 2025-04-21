package com.example.login_api.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponse {
    private Long cartId;
    private Long userId;
    private String imageUrl;
    private List<CartItemResponse> items;
    private double totalPrice;
}
