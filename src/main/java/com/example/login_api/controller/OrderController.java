package com.example.login_api.controller;


import com.example.login_api.entity.Order;
import com.example.login_api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
     // create order
    @PostMapping("/create/{userId}")
     public ResponseEntity<Order> createOrder(@PathVariable Long userId) {
         Order order = orderService.createOrder(userId);
         return ResponseEntity.ok(order);
     }
}
