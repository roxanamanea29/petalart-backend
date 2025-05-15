package com.example.login_api.dto;


import com.example.login_api.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long userId;
    private String orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
}
