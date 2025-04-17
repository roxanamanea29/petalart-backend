package com.example.login_api.repository;

import com.example.login_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);// metodo para buscar una categoria por su nombre
    boolean existsByCategoryName(String categoryName);// metodo para verificar si una categoria existe por su nombre
}