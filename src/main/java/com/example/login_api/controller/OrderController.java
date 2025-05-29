package com.example.login_api.controller;

import com.example.login_api.dto.OrderRequest;
import com.example.login_api.dto.OrderResponse;
import com.example.login_api.security.UserPrincipal;
import com.example.login_api.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/order")
@AllArgsConstructor
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
    @PostMapping("/create")
     public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
         OrderResponse order = orderService.createOrder(userId, request);
         return ResponseEntity.ok(order);
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
