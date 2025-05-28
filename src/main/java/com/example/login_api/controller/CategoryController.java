package com.example.login_api.controller;

import com.example.login_api.dto.CategoryRequest;
import com.example.login_api.dto.CategoryResponse;

import com.example.login_api.dto.CategoryWithProducts;
import com.example.login_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    //metodo utiñizado para crear una categoría
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }

    //metodo utilizado para listar todas las categorías

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.findAll();
    }


    //  Primero el endpoint literal
    @GetMapping("/with-products")
    public List<CategoryWithProducts> getCategoriesWithProducts() {
        return categoryService.categoryWithProducts();
    }

    //  Después el que usa parámetro de ruta
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.findResponseById(id);
    }
    //metodo utilizado para actualizar una categoría
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return categoryService.update(id, request); // actualiza la categoría y la devuelve como DTO
    }

    //metodo para eliminar una categoría
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
