package com.example.login_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;

    private Long categoryId;
    private String categoryName;
}