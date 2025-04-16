package com.mosaic.domain.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantUpdateRequest {
    String color;
    String sizeDescription;
    Integer stockQuantity;
    String updatedBy;
}
