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
    Integer stockQuantity;
    Long imageId;
    boolean isMainUrlImage;
}
