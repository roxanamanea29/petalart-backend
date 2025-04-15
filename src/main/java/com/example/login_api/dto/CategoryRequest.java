package com.example.login_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private String categoryName;
    private String description;
    private String imageUrl;
}
