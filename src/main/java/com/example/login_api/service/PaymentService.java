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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
/*
* Servicio para manejar los pagos de los pedidos.
* Contiene métodos para crear un pago, buscar pagos por ID de usuario y actualizar el estado de un pago.
* */
@Service
@RequiredArgsConstructor
public class PaymentService {

    // Repositorios necesarios para acceder a los datos de pedidos y pagos
    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;

    // guardar pago
    public Payment createPayment(PaymentRequest paymentRequest, UserEntity userEntity) {
        // buscar pedido por id
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
//se crea la instancia de la clase Payment
   Payment payment = new Payment();
        //se le asigna el id del pedido
        payment.setOrderId(String.valueOf(order.getId()));
        //se le asigna el usuario que realiza el pago
        payment.setUser(userEntity);
        //se le asigna el total del pedido
        payment.setTotalAmount(paymentRequest.getTotalAmount());
        //se le asigna el metodo de pago
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        //se le asigna la el estado del pago
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        // genera un id de transacción único simulando la transacción
        payment.setTransactionId("TXN-" + System.currentTimeMillis());

        // se verifica si el metodo de pago es tarjeta de credito
        if(paymentRequest.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
            //se le asigna el estado de pago completado
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
        } else {
            //se le asigna el estado de pago pendiente
            payment.setPaymentStatus(PaymentStatus.PENDING);
        }
        // guardar el pago en la base de datos
        return paymentRepository.save(payment);
    }

    // método para buscar pago por id del usuario
    public List<PaymentResponse> getPaymentByUserId(Long userId) {
        //se busca el pago por id del usuario
      List <Payment> payment = paymentRepository.findByUserId(userId);

      //se mapea el pago a una lista de respuesta
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
              // colecta los resultados en una lista
                .toList();
    }

    // metodo para modificar el estado de un pago utilizando el id del pago y el estado del pago
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        // buscar el pago por id
        Payment payment = paymentRepository.findById(paymentId)
                // si no se encuentra, lanza una excepción
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + paymentId));
        // actualizar el estado del pago
        payment.setPaymentStatus(status);
        // guardar el pago actualizado en la base de datos
        return paymentRepository.save(payment);
    }
}
