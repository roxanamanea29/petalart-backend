package com.example.login_api.repository;

import com.example.login_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName); // metodo para buscar un producto por su nombre
    boolean existsByProductName(String productName); // metodo para verificar si un producto existe por su nombre
    List<Product> findByCategory_CategoryId(Long categoryId);
}