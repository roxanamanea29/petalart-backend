package com.example.login_api.controller;

import com.example.login_api.dto.PaymentRequest;
import com.example.login_api.entity.Payment;
import com.example.login_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    // Inyección de dependencias
    private final PaymentService paymentService;

    // Implementación de métodos para manejar pagos
    // Crear pago
    public ResponseEntity<Payment> createPayment(Payment payment) {
        Payment createdPayment = paymentService.createPayment(new PaymentRequest());
        return ResponseEntity.ok(createdPayment);
    }
}
