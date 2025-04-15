package com.example.login_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithProducts {
    private Long categoryId;
    private String categoryName;
    private List<ProductSummary> products;

}
