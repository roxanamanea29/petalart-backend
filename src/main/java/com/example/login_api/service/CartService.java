package com.example.login_api.service;

import com.example.login_api.dto.CartItemResponse;
import com.example.login_api.dto.CartResponse;
import com.example.login_api.entity.Cart;
import com.example.login_api.entity.CartItem;
import com.example.login_api.entity.Product;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.repository.ICartItemRepository;
import com.example.login_api.repository.ICartRepository;
import com.example.login_api.repository.IProductRepository;
import com.example.login_api.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final ICartItemRepository cartItemRepository;
    private final IProductRepository productRepository;
    private final ModelMapper modelMapper;

    // Obtener el carrito de un usuario por su ID
    public Cart getCartByUserId(Long userId) {
        // Validar el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        // Buscar el carrito del usuario
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    // Si no existe, crea un nuevo carrito asociado al usuario
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });
    }

    public CartResponse getCartResponseByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });

        return convertToCartResponse(cart);
    }


    // Agregar un producto al carrito
    @Transactional
    public CartResponse addProductToCart(Long userId, Long productId, int quantity) {
        try {
            //validar la cantidad
            if (quantity <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor que cero para agregar al carrito.");
            }
            // Obtener el carrito del usuario usando el método getCartByUserId
            Cart cart = getCartByUserId(userId);
            // Verificar si el producto existe
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
            // Verificar si el producto ya está en el carrito
            Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProduct(cart, product);
            // Si el producto ya está en el carrito, actualizar la cantidad
            CartItem item = existingItemOpt.orElseGet(() -> {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(0);
                return newItem;
            });
            // Actualizar la cantidad del producto en el carrito
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            // Asegurarse de que el producto esté en la lista de items del carrito
            if (!cart.getItems().contains(item)) {
                cart.getItems().add(item);
            }
            // Actualizar el total del carrito
            updateCartTotal(cart);
            return convertToCartResponse(cart); // devuelves DTO limpio
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar el producto al carrito: " + e.getMessage(), e);
        }
    }


    // Vaciar el carrito
    @Transactional
    public void clearCart(Long userId) {
        // Obtener el carrito del usuario usando el método getCartByUserId
       try {
        Cart cart = getCartByUserId(userId);
           // elimina todos los cartitems de la base de datos de ese carrito
        cartItemRepository.deleteAll(cart.getItems());
        // Limpia la lista de items del carrito de la memoria
        cart.getItems().clear();
        //actualiza el total en 0
        updateCartTotal(cart);
    }
       // Si ocurre un error, lanzar una excepción personalizada
       catch (Exception e) {
              throw new RuntimeException("Error al vaciar el carrito: " + e.getMessage(), e);
         }
     }

    /* Recalcular el total del carrito . Al utilizaer el método @Transactional,
     se asegura que todas las operaciones dentro del método se ejecuten en una transacción.
     o se ejecuta todo o nos e ejecuta nada.
     para evitar que se guarde información incorrecta en la base de datos.*/
    @Transactional
    public Cart updateCartTotal(Cart cart) {
        // Verificar si el carrito tiene items
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }
        // Calcular el total del carrito
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Actualizar el total del carrito
        cart.setTotalPrice(total);
        cart.setUpdatedAt(LocalDateTime.now());
        // Guardar el carrito actualizado
        return cartRepository.save(cart);
      }

    public CartResponse updateProductQuantity(Long userId, Long productId,int newQuantity){
      try {


        // Obtener el carrito del usuario
        Cart cart = getCartByUserId(userId);
        // Verificar si el producto existe
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
        // Verificar si el producto está en el carrito
        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("El producto no está en el carrito."));
        // Actualizar la cantidad del producto en el carrito
        if (newQuantity <= 0) {
            System.out.println("Eliminando producto del carrito con ID: " + productId);
            cartItemRepository.delete(item);
            cart.getItems().removeIf(i -> i.getProduct().getProductId().equals(productId));
        } else {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }
        System.out.println("Actualizando cantidad del producto en el carrito con ID: " + productId);
        return convertToCartResponse(updateCartTotal(cart));
    }
        catch (Exception e) {
                throw new RuntimeException("Error al actualizar la cantidad del producto en el carrito: " + e.getMessage(), e);
             }
        }

// Obtener el carrito de un usuario (por objeto User)
public CartResponse getCartResponseByUserId(Long userId) {
    try {
        // Validar el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        // Buscar el carrito del usuario
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    // Si no existe, crea un nuevo carrito asociado al usuario
                    Cart newCart = new Cart();
                    // Asignar el usuario al nuevo carrito
                    newCart.setUser(user);
                    // Inicializa la lista de items y el total del carrito
                    newCart.setItems(new ArrayList<>());
                    // Inicializa el total del carrito a cero
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    // Establece las fechas de creación y actualización
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    // Guarda el nuevo carrito en la base de datos
                    return cartRepository.save(newCart);
                });
        // Convertir el carrito a DTO y lo devolve
        return convertToCartResponse(cart);

    } catch (Exception e) {
        throw new RuntimeException("Error al obtener el carrito del usuario: " + e.getMessage(), e);
    }
}

    // Obtener el carrito de un usuario (por objeto Cart)
    private CartResponse convertToCartResponse(Cart cart) {
       try {
           //mapea el carrito a CartResponse
        CartResponse dto = modelMapper.map(cart, CartResponse.class);

        // Si el carrito no tiene items, inicializa la lista para evitar NullPointerException
        List<CartItemResponse> itemDtos = cart.getItems().stream().map(item -> {
            // Mapea cada CartItem a CartItemResponse
            CartItemResponse itemDto = new CartItemResponse();
            itemDto.setProductId(item.getProduct().getProductId());
            itemDto.setProductName(item.getProduct().getProductName());
            itemDto.setDescription(item.getProduct().getDescription());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getProduct().getPrice());
            itemDto.setImageUrl(item.getProduct().getProductImage());
            //
            return itemDto;
        }).toList();

        dto.setItems(itemDtos);
        return dto;
    }
         catch (Exception e) {
                throw new RuntimeException("Error al convertir el carrito a DTO: " + e.getMessage(), e);
          }
     }
}
