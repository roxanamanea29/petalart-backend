package com.example.login_api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    private Long productId;
    @JsonProperty("name")
    private String productName;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    @JsonProperty("imageUrl")
    private String productImage;
    private String categoryName;
}
