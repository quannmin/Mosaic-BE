package com.mosaic.service.spec;

import com.mosaic.domain.cart.Cart;
import com.mosaic.entity.ProductVariant;

public interface CartService {
    Cart getCart(Long userId);
    Cart addCart(Long userId, Long productVariantId, int quantity);
    Cart removeFromCart(Long userId, Long productVariantId);
    Cart clearCart(Long userId);
    Cart removeSpecificProductFromCart(Long userId, Long productVariantId);
}
