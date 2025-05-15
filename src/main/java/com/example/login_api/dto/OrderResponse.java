package com.example.login_api.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private LocalDateTime date;
    private BigDecimal total;
    private List<OrderItemResponse> items;
    private List<AddressResponse> addresses;
}
