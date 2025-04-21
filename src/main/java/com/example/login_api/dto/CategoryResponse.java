package com.example.login_api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class CategoryResponse {
    private Long id;
    private String categoryName;
    private String description;
    private String imageUrl;
}
