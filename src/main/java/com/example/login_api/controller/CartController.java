package com.example.login_api.controller;

import com.example.login_api.dto.AddProductRequest;
import com.example.login_api.dto.CartResponse;
import com.example.login_api.dto.UpdateQuantityRequest;
import com.example.login_api.repository.IUserRepository;
import com.example.login_api.security.UserPrincipal;
import com.example.login_api.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/cart")

public class CartController {
    private final CartService cartService;
    private final IUserRepository userRepository;

    public CartController(CartService cartService, IUserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // get cart
    @GetMapping("/my-cart")
    public ResponseEntity<CartResponse> getMyCart(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(cartService.getCartResponseByEmail(email));
    }

    // add product to cart
    @PostMapping("/add")
    public CartResponse addProductToCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AddProductRequest request) {

       Long userId = userPrincipal.getUserId();
        return cartService.addProductToCart(
                userId,
                request.getProductId(),
                request.getQuantity()
        );
    }
    // remove product from cart old
   /* @DeleteMapping("/remove/{userId}/{productId}")
    public void removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
    }*/
    // remove product from cart para recuperar el carrito despues de eliminar un producto

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity <CartResponse> removeProductFromCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long productId) {
        Long userId = userPrincipal.getUserId();
        CartResponse updatedCart = cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    // update product quantity utilizando el principal para descarter malas inyecciones
    @PutMapping("/update")
    public CartResponse updateQuantity(@RequestBody UpdateQuantityRequest request, Principal principal) {
        // Get the userId from the Principal object
        String email = principal.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encotrado con este email: " + email))
                .getId();

        return cartService.updateProductQuantity(
                userId,
                request.getProductId(),
                request.getQuantity()
        );
    }
    // clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        cartService.clearCart(userId);
        CartResponse updated = cartService.getCartResponseByUserId(userId);
        return ResponseEntity.ok(updated);
    }
}
