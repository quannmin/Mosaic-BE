package com.mosaic.domain.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    BigInteger id;
    String name;
    BigDecimal costPrice;
    BigDecimal retailPrice;
    BigDecimal discountPrice;
    Integer discountPercentage;
    String sizeDescription;
    String description;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    String mainImageUrl;
}
