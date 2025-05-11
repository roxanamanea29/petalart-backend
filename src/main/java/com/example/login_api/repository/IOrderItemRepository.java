package com.example.login_api.repository;


import com.example.login_api.entity.Order;
import com.example.login_api.entity.OrderItem;
import com.example.login_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
    //devuelve todos los orderItems de una orden
    List<OrderItem> findByOrder(Order order);

    //elimina un producto de la orden
    void deleteByOrderAndProduct(Order order, Product product);

    //verifica si existe un producto en esa orden para sumarle cantidad
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);
}
