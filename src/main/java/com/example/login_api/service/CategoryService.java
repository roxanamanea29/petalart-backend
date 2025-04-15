package com.example.login_api.service;

import com.example.login_api.dto.CategoryRequest;
import com.example.login_api.dto.CategoryResponse;
import com.example.login_api.dto.CategoryWithProducts;
import com.example.login_api.dto.ProductSummary;
import com.example.login_api.entity.Category;
import com.example.login_api.entity.Product;
import com.example.login_api.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    //  Inyección del repositorio de categorías
    private final ICategoryRepository categoryRepository;

    //  Listar todas las categorías en formato DTO
    public List<CategoryResponse> findAll() { //busca todas las categorías en la base de datos
        return categoryRepository.findAll()
                .stream()//las transforma en en un flujo de datos para recorrerlas una por una
                .map(this::mapToCategoryResponse)//transforma cada categoria en un categoryResponse
                .collect(Collectors.toList());//las pone en una lista
    }

    // Crear nueva categoría desde DTO
    public CategoryResponse save(CategoryRequest request) {//recibe un DTO(categoryRequest) con los datos de la categoría para guardar
        Category category = new Category();//crea una nueva categoría
        category.setCategoryName(request.getCategoryName());//asigna el nombre de la categoría desde el DTO que recibio
        category.setDescription(request.getDescription());//asigna la descripción de la categoría desde el DTO que recibió
        category.setImageUrl(request.getImageUrl());//asigna la url de la imagen de la categoría desde el DTO que recibió
        Category saved = categoryRepository.save(category);//guarda la categoría en la base de datos
        return mapToCategoryResponse(saved);//devuelve el DTO (lo atributos declarados en el category response) de la categoría guardada
    }

    // Obtener DTO por ID
    public CategoryResponse findResponseById(Long id) {//recibe el id de la categoría a buscar
        Category category = categoryRepository.findById(id)//y lo busca en la base de datos
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con Id: " + id));//si no la encuentra lanza una excepción
        return mapToCategoryResponse(category);//transforma la categoría en un DTO y lo devuelve
    }

    // Actualizar una categoría existente
    public CategoryResponse update(Long id, CategoryRequest request) {
        // busca la categoría por id en la base de datos
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con Id: " + id));
        // asigna el nuevo nombre de la categoría desde el DTO que recibió
        existing.setCategoryName(request.getCategoryName());
        // guarda la categoría actualizada en la base de datos
        existing.setDescription(request.getDescription());
        existing.setImageUrl(request.getImageUrl());
        Category updated = categoryRepository.save(existing);
        // convierte la entidad actualizada a un DTO y lo devuelve
        return mapToCategoryResponse(updated);
    }

    // Eliminar por ID
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {//verifica si existe la categoría por id en la base de datos
            throw new RuntimeException("No se puede eliminar: categoría no encontrada con el Id: " + id); //si no existe lanza una excepción
        }
        categoryRepository.deleteById(id);//si existe la elimina de la base de datos
    }

    // Método para devolver la categoría y una lista con sus productos (solo títulos para el menu)
    public List<CategoryWithProducts> categoryWithProducts() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryWithProducts(
                        category.getCategoryId(),
                        category.getCategoryName(),
                        category.getProducts().stream()
                                .map(product -> new ProductSummary(product.getProductId(), product.getProductName())) //solo devuelve el id y el nombre del producto
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }


    // Convertir entidad a DTO
    private CategoryResponse mapToCategoryResponse(Category category) {//recibe una categoría y la convierte en un DTO
        CategoryResponse response = new CategoryResponse();//crea un nuevo DTO(UNA NUEVA CAJA categoryResponse) para guardar los datos
        response.setId(category.getCategoryId());//asigna el id de la categoría al DTO
        response.setCategoryName(category.getCategoryName());//asigna el nombre de la categoría al DTO
       response.setDescription(category.getDescription());//asigna la descripción de la categoría al DTO
        response.setImageUrl(category.getImageUrl());//asigna la url de la imagen de la categoría al DTO
        return response;//devuelve el DTO
    }
}