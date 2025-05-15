package com.example.login_api.service;

import com.example.login_api.dto.OrderResponse;
import com.example.login_api.dto.PaymentRequest;
import com.example.login_api.dto.PaymentResponse;
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

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    // método para buscar pago por id del usuario
    public List<PaymentResponse> getPaymentByUserId(Long userId) {
      List <Payment> payment = paymentRepository.findByUserId(userId);

      return payment.stream()
                .map(pago -> new PaymentResponse(
                        pago.getId(),
                        pago.getOrderId(),
                        pago.getTotalAmount(),
                        pago.getPaymentMethod(),
                        pago.getPaymentStatus(),
                        pago.getTransactionId(),
                        pago.getCreatedAt(),
                        pago.getUpdatedAt()
                ))
                .toList();
    }




    // modificar el estado del pago de pendiente a completado
    public Payment  updatePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        payment.setPaymentStatus(PaymentStatus.COMPLETED);

        return paymentRepository.save(payment);
    }
}
