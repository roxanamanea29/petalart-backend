package com.example.login_api.dto;

import com.example.login_api.enums.PaymentMethod;
import com.example.login_api.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private String orderId;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor, getters y setters generados automáticamente por Lombok
}
