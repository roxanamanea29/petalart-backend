package com.example.login_api.controller;

import com.example.login_api.dto.ProductRequest;
import com.example.login_api.dto.ProductResponse;
import com.example.login_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //metodo utilizado para listar todos los productos
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.findAll();
    }

    //metodo utilizado para buscar un producto por id
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.findResponseById(id);
    }
    //metodo utilizado para buscar un producto por categoria
    @GetMapping("/by-category/{id}")
    public List<ProductResponse> getProductsByCategory(@PathVariable Long id) {
        return productService.findByCategoryId(id);
    }

    //metodo utilizado para crear un producto
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.save(request);
    }

    //metodo utilizado para actualizar un producto
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return productService.update(id,request);
    }

    //metodo utilizado para eliminar un producto
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }

    //metodo utilizado para buscar productos por nombre o descripcion utilizando un query
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam (name = "query", required = false)  String query) {
        // Verifica si el query es nulo o vacío o solo contiene espacios
        if(query == null || query.trim().isEmpty()) {
            //si es nulo o vacío, retorna una respuesta vde Bad Request
            return ResponseEntity
                    .badRequest()
                    // con un cuerpo vacío
                    .body(Collections.emptyList());
        }
        //si el query no es nulo o vacío, llama al servicio para buscar productos por nombre o descripción
        List<ProductResponse> results = productService.searchByNameOrDescription(query);
        //devuelver la lista de productos encontrados como respuesta
        return ResponseEntity.ok(results);
    }
}