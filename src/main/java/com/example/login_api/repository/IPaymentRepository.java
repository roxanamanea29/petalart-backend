package com.example.login_api.repository;


import com.example.login_api.entity.Payment;
import com.example.login_api.enums.PaymentMethod;
import com.example.login_api.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {

    // buscar pagos por usuario
    List<Payment> findByUserId(Long userId);

    // buscar pagos por el id del pedido
    List<Payment> findByOrderId(String orderId);

    // buscar pagos por estado
    List<Payment> findByPaymentStatus(PaymentStatus status);

   // buscar pagos por método de pago
    List<Payment> findByPaymentMethod(PaymentMethod method);
}
