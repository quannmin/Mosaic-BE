package com.mosaic.domain.request;

import com.mosaic.util.constant.PaymentMethodEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    Long addressId;
    PaymentMethodEnum paymentMethod;
    Double shippingPrice;
    BigDecimal totalOriginalItemsPrice;
    BigDecimal totalAppliedItemsPrice;
    BigDecimal totalPrice;
}
