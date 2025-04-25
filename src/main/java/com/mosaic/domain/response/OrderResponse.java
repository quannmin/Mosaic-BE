package com.mosaic.domain.response;

import com.mosaic.util.constant.OrderStatusEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    String orderNumber;
    OrderStatusEnum status;
    String paymentMethod;
    BigDecimal shippingPrice;
    BigDecimal totalOriginalItemsPrice;
    BigDecimal totalAppliedItemsPrice;
    BigDecimal totalPrice;
    BigDecimal savings;
    Instant createdAt;
    Instant updatedAt;
    List<OrderDetailResponse> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderDetailResponse {
        Long id;
        Long productVariantId;
        Integer quantity;
        BigDecimal originalPrice;
        BigDecimal appliedPrice;
        BigDecimal totalOriginalPrice;
        BigDecimal totalAppliedPrice;
        String productName;
        String color;
        String mainImageUrl;
        String discountReason;
    }
}
