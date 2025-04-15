package com.example.login_api.controller;

import com.example.login_api.dto.CategoryRequest;
import com.example.login_api.dto.CategoryResponse;

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

    //esta clase es la que se encarga de manejar las peticiones HTTP de las categorías
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

    //metodo utilizado para buscar una categoría por id
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.findResponseById(id); // busca la categoría por id y la devuelve en formato DTO
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
