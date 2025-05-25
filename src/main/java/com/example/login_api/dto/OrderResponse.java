package com.example.login_api.dto;


import com.example.login_api.enums.OrderStatus;
import com.example.login_api.enums.PaymentMethod;
import com.example.login_api.enums.PaymentStatus;
import com.example.login_api.enums.ShippingMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private LocalDateTime date;
    private BigDecimal total;
    private List<OrderItemResponse> items;
    private List<AddressResponse> addresses;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String shippingMethod;

}
