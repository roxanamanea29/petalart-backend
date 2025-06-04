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

    /**
     * 1) Obtiene (o crea) el Carrito del usuario.
     *    - No está anotado @Transactional porque solo lee o crea una nueva entidad Cart.
     */
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

    /**
     * 2) Devuelve un DTO con el Carrito del usuario (buscando por email).
     *    - Funcionalmente idéntico a getCartByUserId pero mapea a CartResponse.
     */
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


    /**
     * 3) Añade un producto al carrito.
     *    - Anotado @Transactional, para que toda la operación (insert/update de CartItem y recálculo de total)
     *      se ejecute en la misma transacción.
     */
    @Transactional
    public CartResponse addProductToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero para agregar al carrito.");
        }

        Cart cart = getCartByUserId(userId); // puede devolver un carrito nuevo si no existe

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
        cartItemRepository.save(item); // persiste/actualiza CartItem

        if (!cart.getItems().contains(item)) {
            cart.getItems().add(item);
        }

        // Recalcular total y guardar ambos: CartItem y Cart
        Cart cartActualizado = updateCartTotal(cart);
        return convertToCartResponse(cartActualizado);
    }

    /**
     * 3.1) Elimina un producto del carrito.
     *     - Anotado @Transactional para que se ejecute en la misma transacción.
     *     - Si el CartItem no existe, lanza una excepción.
     **/

    @Transactional
    public CartResponse removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("El producto no está en el carrito."));

        // Elimina el CartItem y actualiza el carrito
        cartItemRepository.delete(item);
        cart.getItems().removeIf(i -> i.getProduct().getProductId().equals(productId));

        // Recalcula el total y persiste
        Cart cartActualizado = updateCartTotal(cart);
        return convertToCartResponse(cartActualizado);
    }
    /**
     * 4) Vacía un carrito completo (elimina todos los CartItem y reinicia totales en 0).
     *    - Anotado @Transactional.
     */
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        // Borra todos los CartItem en bloque
        cartItemRepository.deleteAll(cart.getItems());
        // Reinicia la lista y totales en memoria
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setUpdatedAt(LocalDateTime.now());
        // Persiste los cambios en Cart
        cartRepository.save(cart);
    }

    /**
     * 5) Recalcula total y persiste Carrito.
     *    - Está anotado @Transactional para asegurar que el método se ejecute dentro de la misma transacción
     *      (si es llamado desde otro método @Transactional, se reutiliza la transacción actual).
     */
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

    /**
     * 6) Actualiza la cantidad de un único producto en el carrito.
     *    - Ahora anotado @Transactional para que se guarde correctamente el carrito tras la modificación.
     *    - Si la nueva cantidad es ≤ 0, elimina el CartItem; en otro caso actualiza la cantidad.
     *    - Luego recalcula total mediante updateCartTotal(cart).
     */
    @Transactional
    public CartResponse updateProductQuantity(Long userId, Long productId, int newQuantity) {
        Cart cart = getCartByUserId(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("El producto no está en el carrito."));

        if (newQuantity <= 0) {
            cartItemRepository.delete(item);
            cart.getItems().removeIf(i -> i.getProduct().getProductId().equals(productId));
        } else {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }

        // Recalcula el total y persiste
        Cart cartActualizado = updateCartTotal(cart);
        return convertToCartResponse(cartActualizado);
    }

    /**
     * 7) Devuelve un CartResponse para un usuario dado (por userId).
     *    - Internamente crea un nuevo carrito si no existe.
     */
    public CartResponse getCartResponseByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
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


    /**
     * 8) Mapea la entidad Cart a un DTO CartResponse, incluyendo sus CartItemResponse.
     */
    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse dto = modelMapper.map(cart, CartResponse.class);

        List<CartItemResponse> itemDtos = cart.getItems().stream()
                .map(item -> {
                    CartItemResponse itemDto = new CartItemResponse();
                    itemDto.setProductId(item.getProduct().getProductId());
                    itemDto.setProductName(item.getProduct().getProductName());
                    itemDto.setDescription(item.getProduct().getDescription());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getProduct().getPrice());
                    itemDto.setImageUrl(item.getProduct().getProductImage());
                    return itemDto;
                })
                .toList();

        dto.setItems(itemDtos);
        return dto;
    }
}