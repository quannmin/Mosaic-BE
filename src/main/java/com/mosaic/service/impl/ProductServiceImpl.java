package com.mosaic.service.impl;

import com.mosaic.domain.request.ProductCreateRequest;
import com.mosaic.domain.request.ProductUpdateRequest;
import com.mosaic.domain.response.ProductResponse;
import com.mosaic.entity.Product;
import com.mosaic.exception.ElementNotFoundException;
import com.mosaic.mapper.ProductMapper;
import com.mosaic.repository.ProductRepository;
import com.mosaic.service.spec.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final S3Service s3Service;

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ElementNotFoundException("Product not found"));
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest, MultipartFile image) {
        String imageUrl = null;
        if(image != null && !image.isEmpty()) {
            imageUrl = s3Service.uploadProductImage(image);
        }

        Product product = productMapper.toProductCreate(productCreateRequest);
        product.setMainImageUrl(imageUrl);
        product.setCreatedBy("");
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest productUpdateRequest, MultipartFile image) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ElementNotFoundException("Product not found"));

        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.uploadProductImage(image);
            s3Service.deleteImage(existingProduct.getMainImageUrl());
            existingProduct.setMainImageUrl(imageUrl);
        }

        productMapper.toProductUpdate(productUpdateRequest, existingProduct);
        existingProduct.setUpdatedBy("");

        return productMapper.toProductResponse(productRepository.save(existingProduct));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ElementNotFoundException("Product not found"));
        s3Service.deleteImage(product.getMainImageUrl());
        productRepository.delete(product);
    }
}
