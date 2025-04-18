package com.mosaic.domain.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    BigDecimal totalOriginalPrice;
    BigDecimal totalAppliedPrice;
    BigDecimal totalPrice;
    BigDecimal savings;
    List<CartItem> items;
}
