package com.example.login_api.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class AddProductRequest {
    private Long productId;
    private int quantity;
}
