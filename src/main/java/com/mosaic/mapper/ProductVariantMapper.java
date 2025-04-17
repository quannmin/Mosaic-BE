package com.mosaic.mapper;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.request.ProductVariantUpdateRequest;
import com.mosaic.domain.response.ProductVariantResponse;
import com.mosaic.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);
    ProductVariant toProductVariantCreate(ProductVariantCreateRequest productVariantCreateRequest);
    ProductVariant toProductVariantUpdate(ProductVariantUpdateRequest productVariantUpdateRequest, @MappingTarget ProductVariant existingProductVariant);
}
