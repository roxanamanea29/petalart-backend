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

    //
   /* @GetMapping("my-cart")
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCartResponseByUserId(1L));
    }*/

    // add product to cart
    @PostMapping("/add")
    public CartResponse addProductToCart(@RequestBody AddProductRequest request, @AuthenticationPrincipal UserPrincipal userprincipal) {
        return cartService.addProductToCart(userprincipal.getUserId(), request.getProductId(), request.getQuantity());
    }
    // remove product from cart old
   /* @DeleteMapping("/remove/{userId}/{productId}")
    public void removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
    }*/
    // remove product from cart para recuperar el carrito despues de eliminar un producto

    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity <CartResponse> removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        CartResponse updatedCart = cartService.getCartResponseByUserId(userId);
        return ResponseEntity.ok(updatedCart);
    }
    // update product quantity old
 /*   @PutMapping("/update")
    public CartResponse updateQuantity(@RequestBody AddProductRequest request) {
        return cartService.updateProductQuantity(
                request.getUserId(),
                request.getProductId(),
                request.getQuantity()
        );
    }*/
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
    @DeleteMapping("/clear/{userId}")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}
