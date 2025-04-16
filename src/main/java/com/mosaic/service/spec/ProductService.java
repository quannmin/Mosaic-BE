package com.mosaic.service.spec;
import com.mosaic.domain.request.ProductCreateRequest;
import com.mosaic.domain.request.ProductUpdateRequest;
import com.mosaic.domain.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse getProductById(Long productId);
    ProductResponse createProduct(ProductCreateRequest productCreateRequest, MultipartFile image);
    ProductResponse updateProduct(Long productId, ProductUpdateRequest productUpdateRequest, MultipartFile image);
    List<ProductResponse> getAllProducts();
    boolean deleteProduct(Long productId);
}
