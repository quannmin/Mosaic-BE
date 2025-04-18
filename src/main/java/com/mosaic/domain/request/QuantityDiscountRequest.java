package com.mosaic.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuantityDiscountRequest {
    @NotNull
    Long productVariantId;
    @NotNull
    @Min(2)
    Integer minQuantity;
    @NotNull
    @Min(1)
    @Max(100)
    Integer discountPercentage;
}
