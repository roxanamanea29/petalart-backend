package com.example.login_api.service;


import com.example.login_api.dto.ProductRequest;
import com.example.login_api.dto.ProductResponse;
import com.example.login_api.entity.Category;
import com.example.login_api.entity.Product;
import com.example.login_api.repository.ICategoryRepository;
import com.example.login_api.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ICategoryRepository categoryRepository; //inicializa el repositorio de categorias

    //injectamos el repositorio de productos
    private final IProductRepository productRepository;

    // Add methods to interact with the product repository as needed
   public List<ProductResponse> findAll(){
         return productRepository.findAll()
                 .stream()
                 .map(this::mapToProductResponse)
                 .collect(Collectors.toList());

   }
   //metodo para guardar un porducto
    public ProductResponse save(ProductRequest request) {
       Product product = new Product(); //crea un nuevo producto
         product.setProductName(request.getProductName()); //asigna el nombre del producto desde el DTO que recibio
         product.setPrice(request.getPrice()); //asigna el precio del producto des+de el DTO que recibio
            product.setDescription(request.getDescription()); //asigna la descripción del producto desde el DTO que recibio
            product.setProductImage(request.getProductImage()); //asigna la url de la imagen del producto desde el DTO que recibio
                 if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("El categoryId no puede ser null");
        }
            Category category = categoryRepository.findById(request.getCategoryId()) //busca la categoría por id
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con Id: " + request.getCategoryId())); //si no la encuentra lanza una excepción
            product.setCategory(category); //asigna la categoría al producto

            Product saved = productRepository.save(product); //guarda el producto en la base de datos
            return mapToProductResponse(saved); //devuelve el DTO (lo atributos declarados en el product response) del producto guardado




   }

   //buscar un producto por el ID
    public ProductResponse findResponseById(Long id){
       Product product = productRepository.findById(id)  //busca el producto por id
               .orElseThrow(() -> new RuntimeException("Producto no encontrado con Id: "+ id));
        return mapToProductResponse(product); //transforma el producto en un DTO y lo devuelve.
    }

    //buscar un producto por el id de la categoria
    public List<ProductResponse> findByCategoryId(Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }


    //actualiza un producto
    public ProductResponse update(Long id, ProductRequest request){
       //busca el producto por el id
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con Id: "+ id));
        existing.setProductName(request.getProductName()); //asigna el nombre del producto desde el DTO que recibio
        existing.setPrice(request.getPrice()); //asigna el precio del producto desde el DTO que recibio
        existing.setDescription(request.getDescription()); //asigna la descripción del producto desde el DTO que recibio
        existing.setProductImage(request.getProductImage()); //asigna la url de la imagen del producto desde el DTO que recibio
        Product updated = productRepository.save(existing); //guarda el producto actualizado en la base de datos
        return mapToProductResponse(updated); //transforma el producto en un DTO y lo devuelve.
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no  encontrado con Id: "+ id));
    }
   //elimina un producto por id

    public void deleteById(Long id) {
        if(!productRepository.existsById(id)) {//verifica si existe el producto por id en la base de datos
            throw new RuntimeException("No se puede eliminar: producto no encontrado con el Id: " + id); //si no existe lanza una excepción
        }
        productRepository.deleteById(id);//si existe lo elimina de la base de datos
    }
    private ProductResponse mapToProductResponse(Product product) {//recibe un producto y lo transforma en un DTO
        ProductResponse response = new ProductResponse();//crea un nuevo DTO para el producto
        response.setId(product.getProductId());//asigna el id del producto al DTO
        response.setName(product.getProductName());//asigna el nombre del producto al DTO
        response.setPrice(product.getPrice());//asigna el precio del producto al DTO
        response.setDescription(product.getDescription());//asigna la descripción del producto al DTO
        response.setImageUrl(product.getProductImage());//asigna la url de la imagen del producto al DTO
       if(product.getCategory() != null) { //verifica si la categoría no es nula
            response.setCategoryId(product.getCategory().getCategoryId()); //asigna el id de la categoría al DTO
            response.setCategoryName(product.getCategory().getCategoryName()); //asigna el nombre de la categoría al DTO
        }
        return response;//devuelve el DTO
    }
}
