package com.example.login_api.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {

    private Long userId;
    private List<Long> addressIds;
    private List<OrderItemRequest> items;
    private BigDecimal total;
    private LocalDateTime date;
}
