package com.mosaic.domain.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartCreateRequest {
     Long userId;
     Long productVariantId;
     int quantity;
}
