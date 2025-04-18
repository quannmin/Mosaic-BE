package com.mosaic.controller;

import com.mosaic.domain.cart.Cart;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.service.spec.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<Cart>> addCart(Long userId, Long productVariantId, int quantity) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Cart>builder()
                        .success(true)
                        .message("Add to cart successfully!")
                        .code(HttpStatus.CREATED.value())
                        .data(cartService.addCart(userId, productVariantId, quantity))
                        .build());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Cart>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                        .success(true)
                        .message("Get cart successfully!")
                        .code(HttpStatus.OK.value())
                        .data(cartService.getCart(userId))
                        .build());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Cart>> deleteCart(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .success(true)
                .message("Delete cart successfully!")
                .code(HttpStatus.OK.value())
                .data(cartService.clearCart(userId))
                .build());
    }

    @DeleteMapping("/users/{userId}/product-variants/{ProductVariantId}/remove-quantity")
    public ResponseEntity<ApiResponse<Cart>> deleteFromCart(@PathVariable Long userId, @PathVariable Long ProductVariantId) {
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .success(true)
                .message("Remove quantity item from cart successfully!")
                .code(HttpStatus.OK.value())
                .data(cartService.removeFromCart(userId, ProductVariantId))
                .build());
    }

    @DeleteMapping("/users/{userId}/product-variants/{ProductVariantId}")
    public ResponseEntity<ApiResponse<Cart>> deleteSpecificProductFromCart(@PathVariable Long userId, @PathVariable Long ProductVariantId) {
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .success(true)
                .message("Delete cart item successfully!")
                .code(HttpStatus.OK.value())
                .data(cartService.removeSpecificProductFromCart(userId, ProductVariantId))
                .build());
    }
}
