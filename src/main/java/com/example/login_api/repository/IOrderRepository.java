package com.example.login_api.repository;


import com.example.login_api.entity.Order;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, encontrar órdenes por estado, usuario,
    // Busca un order pos etc.
     Optional<Order> findByStatus(OrderStatus status);
    List<Order> findByUser(UserEntity user);

}
