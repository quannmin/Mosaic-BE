package com.mosaic.mapper;

import com.mosaic.domain.request.ProductCreateRequest;
import com.mosaic.domain.request.ProductUpdateRequest;
import com.mosaic.domain.response.ProductResponse;
import com.mosaic.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
    Product toProductCreate(ProductCreateRequest productCreateRequest);
    void toProductUpdate(ProductUpdateRequest productUpdateRequest, @MappingTarget Product existingProduct);
}
