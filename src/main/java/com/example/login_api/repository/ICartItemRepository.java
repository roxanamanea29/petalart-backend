package com.example.login_api.repository;

import com.example.login_api.entity.Cart;
import com.example.login_api.entity.CartItem;
import com.example.login_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    //devuelve todos los cartItems de un carrito
    List<CartItem> findByCart(Cart cart);

    //elimina un producto del carrito
    void deleteByCartAndProduct(Cart cart, Product product);

    //verifica si existe un producto en ese carrito para sumarle cantidad
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}

