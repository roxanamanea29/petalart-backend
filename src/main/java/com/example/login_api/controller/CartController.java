package com.example.login_api.controller;


import com.example.login_api.dto.AddProductRequest;
import com.example.login_api.dto.CartResponse;
import com.example.login_api.entity.Cart;
import com.example.login_api.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")

public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // get cart
    @GetMapping("/user/{userId}")
    public CartResponse getCart(@PathVariable Long userId) {
        return cartService.getCartResponseByUserId(userId);
    }

    // add product to cart
    @PostMapping("/add")
    public CartResponse addProductToCart(@RequestBody AddProductRequest request){
        return cartService.addProductToCart(request.getUserId(), request.getProductId(), request.getQuantity());
    }
    // remove product from cart
    @DeleteMapping("/remove/{userId}/{productId}")
    public void removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
    }
    @PutMapping("/update")
    public CartResponse updateQuantity(@RequestBody AddProductRequest request) {
        return cartService.updateProductQuantity(
                request.getUserId(),
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
