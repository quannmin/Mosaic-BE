package com.mosaic.domain.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantCreateRequest {
    String color;
    Integer stockQuantity;
}
