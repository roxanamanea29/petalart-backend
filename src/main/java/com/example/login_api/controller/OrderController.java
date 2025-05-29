package com.example.login_api.controller;

import com.example.login_api.dto.OrderRequest;
import com.example.login_api.dto.OrderResponse;
import com.example.login_api.security.UserPrincipal;
import com.example.login_api.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Slf4j
public class OrderController {


    private final OrderService orderService;


    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Admin o debug
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // Usuario autenticado
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = Long.valueOf(userPrincipal.getUserId());
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
    // get un solo order por id
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

     // create order
   /* @PostMapping("/create")
     public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
         OrderResponse order = orderService.createOrder(userId, request);
         return ResponseEntity.ok(order);
     }*/
     @PostMapping("/create")
     public ResponseEntity<?> createOrder(
             @RequestBody OrderRequest request,
             @AuthenticationPrincipal UserPrincipal userPrincipal) {
         try {
             long userId = userPrincipal.getUserId();

             // 🔍 LOGS PARA DEPURACIÓN
             log.info("📥 Pedido recibido de usuario ID={}", userId);
             log.info("📦 Direcciones: {}", request.getAddressIds());
             log.info("🛒 Items: {}", request.getItems());
             log.info("💰 Total: {}", request.getTotal());
             log.info("💳 Método de pago: {}", request.getPaymentMethod());
             log.info("🏠 Tipo de dirección: {}", request.getAddressType());
             log.info("🚚 Método de envío: {}", request.getShippingMethod());
             log.info("📄 Estado del pago: {}", request.getPaymentStatus());

             OrderResponse order = orderService.createOrder(userId, request);
             return ResponseEntity.ok(order);

         } catch (Exception e) {
             log.error("❌ Error al crear el pedido", e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(Map.of(
                             "error", e.getMessage(),
                             "exception", e.getClass().getSimpleName()
                     ));
         }
     }
        //delete order

     @DeleteMapping("/{orderId}")
        public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
         try {
             orderService.deleteOrder(orderId);
             return ResponseEntity.noContent().build();
         } catch (RuntimeException e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message ",e.getMessage()).toString());
         }
     }
}
