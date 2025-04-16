package com.mosaic.service.spec;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.response.ProductResponse;
import com.mosaic.domain.response.ProductVariantResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductVariantService {
    ProductVariantResponse createProductVariant(ProductVariantCreateRequest productVariantCreateRequest, MultipartFile[] image);

}
