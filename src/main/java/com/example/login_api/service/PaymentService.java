package com.example.login_api.service;

import com.example.login_api.dto.OrderResponse;
import com.example.login_api.dto.PaymentRequest;
import com.example.login_api.entity.Order;
import com.example.login_api.entity.Payment;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.enums.PaymentStatus;
import com.example.login_api.repository.IOrderItemRepository;
import com.example.login_api.repository.IOrderRepository;
import com.example.login_api.repository.IPaymentRepository;
import com.example.login_api.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
   private final IUserRepository userRepository;
    // guardar pago

    public Payment createPayment(PaymentRequest paymentRequest) {
        // buscar pedido por id
        Order order = orderRepository.findById(Long.parseLong(paymentRequest.getOrderId()))
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        // buscar usuario por id
        UserEntity user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

   Payment payment = new Payment();
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setUser(user);
        payment.setTotalAmount(paymentRequest.getTotalAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN-" + System.currentTimeMillis());// genera un id de transacción único simulando la transacción

        // guardar el pago en la base de datos
        return paymentRepository.save(payment);
    }

    // buscar pago por id
    // buscar pagos por usuario
    // buscar pagos por estado
    // buscar pagos por método de pago

}
