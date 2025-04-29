package com.mosaic.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mosaic.domain.cart.Cart;
import com.mosaic.domain.cart.CartItem;
import com.mosaic.entity.Image;
import com.mosaic.entity.ProductVariant;
import com.mosaic.entity.QuantityDiscount;
import com.mosaic.exception.custom.InsufficientStockException;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.repository.QuantityDiscountRepository;
import com.mosaic.service.spec.CartService;
import com.mosaic.service.spec.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CART_PREFIX ="cart:";
    private final ProductVariantService productVariantService;
    private final QuantityDiscountRepository quantityDiscountRepository;

    @Override
    public Cart getCart(Long userId) {
        String key = CART_PREFIX + userId;
        Object data = redisTemplate.opsForValue().get(key);
        if (data != null) {
            return objectMapper.convertValue(data, Cart.class);
        }
        return new Cart();
    }

    @Override
    public Cart addCart(Long userId, Long productVariantId, int quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        ProductVariant productVariant = productVariantService.findProductVariantById(productVariantId);

        if (productVariant.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Not enough items in stock");
        }
        Cart cart = getCart(userId);
        boolean itemExists = false;
        for (CartItem existingItem : cart.getItems()) {
            if(existingItem.getProductVariantId().equals(productVariantId)) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }
        if(!itemExists) {
            CartItem cartItem = createNewCartItem(productVariant);
            cartItem.setQuantity(quantity);
            cart.getItems().add(cartItem);
        }
        return updateCart(userId, cart);
    }

    private CartItem createNewCartItem(ProductVariant productVariant) {
        CartItem cartItem = new CartItem();
        cartItem.setProductVariantId(productVariant.getId());
        cartItem.setProductName(productVariant.getProduct().getName());
        cartItem.setColor(productVariant.getColor());
        cartItem.setOriginalPrice(productVariant.getProduct().getRetailPrice());
        productVariant.getImages().stream()
                .filter(Image::isMainUrlImage)
                .findFirst()
                .ifPresent(image -> cartItem.setMainImageUrl(image.getUrlDownload()));
        return cartItem;
    }

    @Override
    public Cart removeFromCart(Long userId, Long productVariantId) {
        Cart cart = getCart(userId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductVariantId().equals(productVariantId))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                cart.getItems().remove(item);
            }
        }

        if(cart.getItems().isEmpty()) {
            redisTemplate.delete(CART_PREFIX + userId);
            return new Cart();
        }
        return updateCart(userId, cart);
    }

    @Override
    public Cart clearCart(Long userId) {
        redisTemplate.delete(CART_PREFIX + userId);
        return new Cart();
    }

    @Override
    public Cart removeSpecificProductFromCart(Long userId, Long productVariantId) {
        Cart cart = getCart(userId);

        boolean productExists = cart.getItems().stream()
                .anyMatch(item -> item.getProductVariantId().equals(productVariantId));

        if (!productExists) {
            throw new ResourceNotFoundException("Product variant in cart", "id", productVariantId);
        }

        cart.getItems().removeIf(item -> item.getProductVariantId().equals(productVariantId));

        if (cart.getItems().isEmpty()) {
            redisTemplate.delete(CART_PREFIX + userId);
            return new Cart();
        }
        return updateCart(userId, cart);
    }

    private Cart updateCart(Long userId, Cart cart) {
        BigDecimal totalOriginalPrice = BigDecimal.ZERO;
        BigDecimal totalAppliedPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            ProductVariant productVariant = productVariantService.findProductVariantById(cartItem.getProductVariantId());

                BigDecimal itemOriginalPrice = cartItem.getOriginalPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                totalOriginalPrice = totalOriginalPrice.add(itemOriginalPrice);

                BigDecimal itemAppliedPrice = calculateAppliedPrice(cartItem, productVariant);
                totalAppliedPrice = totalAppliedPrice.add(itemAppliedPrice);
        }
        cart.setTotalOriginalPrice(totalOriginalPrice);
        cart.setTotalAppliedPrice(totalAppliedPrice);
        cart.setTotalPrice(totalAppliedPrice);
        cart.setSavings(totalOriginalPrice.subtract(totalAppliedPrice));
        redisTemplate.opsForValue().set(CART_PREFIX + userId, cart);
        return cart;
    }

    private BigDecimal calculateAppliedPrice(CartItem item, ProductVariant productVariant) {
        BigDecimal originalPrice = productVariant.getProduct().getRetailPrice();

        BigDecimal appliedPrice = productVariant.getProduct().getDiscountPrice() != null ?
                productVariant.getProduct().getDiscountPrice() : originalPrice;

        List<QuantityDiscount> quantityDiscounts = quantityDiscountRepository
                .findByProductVariantIdOrderByMinQuantityDesc(productVariant.getId());

        for (var quantityDiscount : quantityDiscounts) {
            if(item.getQuantity() >= quantityDiscount.getMinQuantity()) {
                BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                        BigDecimal.valueOf(quantityDiscount.getDiscountPercentage())
                                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));

                BigDecimal discountPrice = originalPrice.multiply(discountMultiplier);
                if(discountPrice.compareTo(appliedPrice) > 0) {
                    appliedPrice = discountPrice;
                    item.setDiscountReason(quantityDiscount.getDiscountPercentage() +
                            "% off when buying " + quantityDiscount.getMinQuantity() + "+ items");
                }
                break;
            }
        }
        item.setAppliedPrice(appliedPrice);
        return appliedPrice;
    }
}
