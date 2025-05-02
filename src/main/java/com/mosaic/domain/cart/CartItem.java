package com.mosaic.domain.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    Long productVariantId;
    int quantity;
    BigDecimal originalPrice;
    BigDecimal appliedPrice;
    BigDecimal totalItemsOriginPrice;
    BigDecimal totalItemsAppliedPrice;
    String productName;
    String color;
    String mainImageUrl;
    String discountReason;
}
