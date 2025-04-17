package com.example.login_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;

    private Long categoryId;
    private String categoryName;
}