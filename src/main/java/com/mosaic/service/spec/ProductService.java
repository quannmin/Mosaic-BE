package com.mosaic.service.spec;
import com.mosaic.domain.request.ProductCreateRequest;
import com.mosaic.domain.request.ProductUpdateRequest;
import com.mosaic.domain.response.ProductResponse;
import com.mosaic.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse findProductResponseById(Long productId);
    Product findProductById(Long productId);
    ProductResponse createProduct(ProductCreateRequest productCreateRequest, MultipartFile image);
    ProductResponse updateProduct(Long productId, ProductUpdateRequest productUpdateRequest, MultipartFile image);
    List<ProductResponse> findAllProducts();
    void deleteProduct(Long productId);
}
