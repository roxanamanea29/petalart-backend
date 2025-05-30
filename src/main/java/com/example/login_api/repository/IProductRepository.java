package com.example.login_api.repository;

import com.example.login_api.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    // Métodos de búsqueda personalizados
    @EntityGraph(attributePaths = {"category"}) // Carga las relaciones de categoría
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategory(); // método para obtener todos los productos

    Optional<Product> findByProductName(String productName); // metodo para buscar un producto por su nombre
    boolean existsByProductName(String productName); // metodo para verificar si un producto existe por su nombre
    List<Product> findByCategory_CategoryId(Long categoryId);


}