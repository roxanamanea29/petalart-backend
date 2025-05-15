package com.example.login_api.controller;

import com.example.login_api.dto.PaymentRequest;
import com.example.login_api.dto.PaymentResponse;
import com.example.login_api.entity.Payment;
import com.example.login_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    // Inyección de dependencias
    private final PaymentService paymentService;

    // Implementación de métodos para manejar pagos
    // crear un nuevo pago
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        Payment createdPayment = paymentService.createPayment(paymentRequest);
        // Crear respuesta de pago
       PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(createdPayment.getId());
        paymentResponse.setTransactionId(createdPayment.getTransactionId());
        paymentResponse.setOrderId(createdPayment.getOrderId());
        paymentResponse.setTotalAmount(createdPayment.getTotalAmount());
        paymentResponse.setPaymentMethod(createdPayment.getPaymentMethod());
        paymentResponse.setPaymentStatus(createdPayment.getPaymentStatus());
        paymentResponse.setCreatedAt(createdPayment.getCreatedAt());
        paymentResponse.setUpdatedAt(createdPayment.getUpdatedAt());

        return ResponseEntity.ok(paymentResponse);
    }

    // buscar pagos por id de usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUser(@PathVariable Long userId) {
        List<PaymentResponse> payments = paymentService.getPaymentByUserId(userId);
        return ResponseEntity.ok(payments);
    }
}