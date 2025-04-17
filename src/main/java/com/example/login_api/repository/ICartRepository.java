package com.example.login_api.repository;


import com.example.login_api.entity.Cart;
import com.example.login_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
    //devuelve un carrito por su id
    Optional<Cart> findByUser(UserEntity user);

}
