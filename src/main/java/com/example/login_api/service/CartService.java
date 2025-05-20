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


    public Cart getCartByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });
    }




    // Agregar un producto al carrito
    @Transactional
    public CartResponse addProductToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("La cantidad debe ser mayorque caero para agregar al carrito.");
        }
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem item = existingItemOpt.orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(0);
            return newItem;
        });

        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);

        updateCartTotal(cart);
        return convertToCartResponse(cart); // devuelves DTO limpio
    }

    // Eliminar un producto del carrito
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        cartItemRepository.deleteByCartAndProduct(cart, product);
        cart.getItems().removeIf(item -> item.getProduct().getProductId().equals(productId));
        updateCartTotal(cart);
    }

    // Vaciar el carrito
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteAll(cart.getItems());// elimina todos los cartitems de la base de datos
        cart.getItems().clear();// elimina todos los items de la lista del carrito en memoria
        updateCartTotal(cart);//actualiza el total en 0
    }

    // Recalcular el total del carrito
    @Transactional
    public Cart updateCartTotal(Cart cart) {
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    public CartResponse updateProductQuantity(Long userId, Long productId,int newQuantity){
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
// Obtener el carrito de un usuario (por objeto User)
public CartResponse getCartResponseByUserId(Long userId) {

    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

    Cart cart  = cartRepository.findByUser(user)
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
    // Obtener el carrito de un usuario (por objeto Cart)
    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse dto = modelMapper.map(cart, CartResponse.class);

        List<CartItemResponse> itemDtos = cart.getItems().stream().map(item -> {
            CartItemResponse itemDto = new CartItemResponse();
            itemDto.setProductId(item.getProduct().getProductId());
            itemDto.setProductName(item.getProduct().getProductName());
            itemDto.setDescription(item.getProduct().getDescription()); // ✅ aquí
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getProduct().getPrice());
            itemDto.setImageUrl(item.getProduct().getProductImage());
            return itemDto;
        }).toList();

        dto.setItems(itemDtos);
        return dto;
    }
}
