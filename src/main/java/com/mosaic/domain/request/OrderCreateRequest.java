package com.mosaic.domain.request;

import com.mosaic.util.constant.PaymentMethodEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest
{
    Long userId;
    PaymentMethodEnum paymentMethod;
    Double shippingPrice;
    BigDecimal totalOriginalItemsPrice;
    BigDecimal totalAppliedItemsPrice;
    BigDecimal totalPrice;
    List<OrderDetailCreateRequest> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderDetailCreateRequest {
        Long productVariantId;
        BigDecimal originalPrice;
        BigDecimal appliedPrice;
        int quantity;
    }
}
