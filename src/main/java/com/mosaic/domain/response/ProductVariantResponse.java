package com.mosaic.domain.response;

import com.mosaic.entity.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    Integer id;
    String color;
    Integer stockQuantity;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    String mainUrlImage;
    List<Image> images;
}
