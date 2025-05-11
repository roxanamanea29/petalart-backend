package com.example.login_api.controller;

import com.example.login_api.dto.OrderItemResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private List<OrderItemResponse> orderItems;
    private String status;
    private String shippingMethod;
    private BigDecimal total;
}
