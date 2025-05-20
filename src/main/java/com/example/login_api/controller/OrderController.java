package com.example.login_api.controller;


import com.example.login_api.dto.OrderRequest;
import com.example.login_api.dto.OrderResponse;
import com.example.login_api.security.UserPrincipal;
import com.example.login_api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/order")

public class OrderController {


    private final OrderService orderService;
    public OrderController( OrderService orderService) {

        this.orderService = orderService;
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
    @PostMapping("/create/{userId}")
     public ResponseEntity<OrderResponse> createOrder(
            @PathVariable Long userId,
            @RequestBody OrderRequest request) {
         OrderResponse order = orderService.createOrder(userId, request);
         return ResponseEntity.ok(order);
     }
}
