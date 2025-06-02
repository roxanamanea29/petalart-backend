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
         return productRepository.findAllWithCategory()
                 .stream()
                 .map(this::mapToProductResponse)
                 .collect(Collectors.toList());

   }
   //metodo para guardar un porducto
    public ProductResponse save(ProductRequest request) {
       ///crea un nuevo producto
       Product product = new Product();
        //asigna el nombre del producto desde el DTO que recibio
         product.setProductName(request.getProductName());
        //asigna el precio del producto desde el DTO que recibio
         product.setPrice(request.getPrice());
        //asigna la descripción del producto desde el DTO que recibio
            product.setDescription(request.getDescription());
        //asigna la url de la imagen del producto desde el DTO que recibio
            product.setProductImage(request.getProductImage());
                 if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("El categoryId no puede ser null");
        }
        //busca la categoría por id
            Category category = categoryRepository.findById(request.getCategoryId())
            //si la encuentra, la asigna al producto sino lanza una excepción
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con Id: " + request.getCategoryId()));
        //asigna la categoría al producto
            product.setCategory(category);
        //guarda el producto en la base de datos
            Product saved = productRepository.save(product);
            //devuelve el DTO (lo atributos declarados en el product response) del producto guardado
            return mapToProductResponse(saved);




   }

   //buscar un producto por el ID
    public ProductResponse findResponseById(Long id){
        //busca el producto existe por id
       Product product = productRepository.findById(id)
                //si existe, devuelve el producto sino
               .orElseThrow(() -> new RuntimeException("Producto no encontrado con Id: "+ id));
       //transforma el producto en un DTO y lo devuelve.
        return mapToProductResponse(product);
    }

    //buscar un producto por el id de la categoria
    public List<ProductResponse> findByCategoryId(Long categoryId) {
       //verifica si la categoría existe por id
        return productRepository.findByCategory_CategoryId(categoryId)
                //si existe, transforma la lista de productos en  lista de DTOs ProductResponse
                .stream()
                // utiliza el método mapToProductResponse para transformar cada producto en un DTO
                .map(this::mapToProductResponse)
                // colecta los resultados en una lista
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

   //elimina un producto por id
    public void deleteById(Long id) {
        if(!productRepository.existsById(id)) {//verifica si existe el producto por id en la base de datos
            throw new RuntimeException("No se puede eliminar: producto no encontrado con el Id: " + id); //si no existe lanza una excepción
        }
        productRepository.deleteById(id);//si existe lo elimina de la base de datos
    }

    //método para buscar productos por nombre o descripción
    public List<ProductResponse> searchByNameOrDescription(String query) {
        //busca los productos que contengan el query en el nombre o descripción
        return productRepository.searchByNameOrDescription(query, query)
                //transforma la lista de productos en una lista de DTOs ProductResponse
                .stream()
                // utiliza el método mapToProductResponse para transformar cada producto en un DTO
                .map(this::mapToProductResponse)
                // colecta los resultados en una lista
                .collect(Collectors.toList());
    }
    //método privado( interno )para mapear un producto al dto ProductResponse  con los atributos
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
