package com.example.login_api.dto;


import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
   private List<OrderItemRequest> items;
}
