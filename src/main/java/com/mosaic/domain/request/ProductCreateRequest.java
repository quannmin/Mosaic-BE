package com.mosaic.domain.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    String name;
    BigDecimal costPrice;
    BigDecimal retailPrice;
    BigDecimal discountPrice;
    Integer discountPercentage;
    String description;
    String sizeDescription;
}
