package com.mosaic.service.spec;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.request.ProductVariantUpdateRequest;
import com.mosaic.domain.response.ProductVariantResponse;
import com.mosaic.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProductVariantService {
    ProductVariantResponse createProductVariant(ProductVariantCreateRequest productVariantCreateRequest, MultipartFile[] image);
    ProductVariantResponse updateProductVariant(Long productVariantId, ProductVariantUpdateRequest productVariantUpdateRequest,
                                                MultipartFile image, MultipartFile[] images);
    ProductVariantResponse findProductVariantById(Long id);
    void deleteProductVariant(Long id);
    List<ProductVariantResponse> findAllProductVariants();
}
