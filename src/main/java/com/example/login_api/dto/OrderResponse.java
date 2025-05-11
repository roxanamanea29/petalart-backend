package com.example.login_api.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private LocalDateTime date;
    private BigDecimal total;
    private List<OrderItemResponse> items;
}
