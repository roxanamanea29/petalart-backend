package com.example.login_api.service;

import com.example.login_api.dto.PaymentRequest;
import com.example.login_api.dto.PaymentResponse;
import com.example.login_api.entity.Order;
import com.example.login_api.entity.Payment;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.enums.PaymentMethod;
import com.example.login_api.enums.PaymentStatus;
import com.example.login_api.repository.IOrderRepository;
import com.example.login_api.repository.IPaymentRepository;
import com.example.login_api.repository.IUserRepository;
import com.example.login_api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final IUserRepository userRepository;

    // guardar pago

    public Payment createPayment(PaymentRequest paymentRequest, UserEntity userEntity) {
        // buscar pedido por id

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

  //
   Payment payment = new Payment();//se crea la instancia de la clase Payment
        payment.setOrderId(String.valueOf(order.getId()));//se le asigna el id del pedido
        payment.setUser(userEntity);//se le asigna el usuario
        payment.setTotalAmount(paymentRequest.getTotalAmount());//se le asigna el total del pedido
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());//se le asigna el metodo de pago
        payment.setPaymentStatus(PaymentStatus.COMPLETED);//
        payment.setTransactionId("TXN-" + System.currentTimeMillis());// genera un id de transacción único simulando la transacción

        if(paymentRequest.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {// se verifica si el metodo de pago es tarjeta de credito
            payment.setPaymentStatus(PaymentStatus.COMPLETED);//se le asigna el estado de pago completado
        } else {
            payment.setPaymentStatus(PaymentStatus.PENDING);//se le asigna el estado de pago pendiente
        }
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

    // metodo para modificar el estado de un pago
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + paymentId));
        payment.setPaymentStatus(status);
        return paymentRepository.save(payment);
    }
}
