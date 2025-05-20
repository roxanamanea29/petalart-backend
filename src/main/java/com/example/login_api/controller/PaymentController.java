package com.example.login_api.controller;

import com.example.login_api.dto.PaymentRequest;
import com.example.login_api.dto.PaymentResponse;
import com.example.login_api.dto.PaymentStatusRequest;
import com.example.login_api.entity.Payment;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.repository.IUserRepository;
import com.example.login_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    // Inyección de dependencias
    private final PaymentService paymentService;
    private final IUserRepository userRepository;


    // Implementación de métodos para manejar pagos
    // crear un nuevo pago
  /*  @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment( PaymentRequest paymentRequest, UserEntity userEntity, Principal principal) {
        // Obtener el id del usuario autenticado
        UserEntity user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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
    }*/

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest, Principal principal) {
        UserEntity userEntity = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Payment createdPayment = paymentService.createPayment(paymentRequest, userEntity);
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

    // buscar pagos por paymentId
    @PostMapping("/update-status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @RequestBody PaymentStatusRequest request) {
        // Lógica para actualizar el estado del pago
        Payment updated = paymentService.updatePaymentStatus(request.getPaymentId(), request.getStatus());


        PaymentResponse response = new PaymentResponse();
        response.setId(updated.getId());
        response.setTransactionId(updated.getTransactionId());
        response.setOrderId(updated.getOrderId());
        response.setTotalAmount(updated.getTotalAmount());
        response.setPaymentMethod(updated.getPaymentMethod());
        response.setPaymentStatus(updated.getPaymentStatus());
        response.setCreatedAt(updated.getCreatedAt());
        response.setUpdatedAt(updated.getUpdatedAt());
        return ResponseEntity.ok(response);
    }

}