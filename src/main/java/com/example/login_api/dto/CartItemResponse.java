package com.example.login_api.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemResponse {
    private Long productId;
    private String productName;
    private String description;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
}
