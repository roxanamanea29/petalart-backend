package com.example.login_api.dto;


import com.example.login_api.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private String transactionId;
}
