package com.mosaic.controller;

import com.mosaic.domain.request.ProductCreateRequest;
import com.mosaic.domain.request.ProductUpdateRequest;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.domain.response.ProductResponse;
import com.mosaic.service.spec.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Get product successfully!")
                .data(productService.findProductResponseById(id))
                .code(HttpStatus.OK.value())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts() {
        return ResponseEntity.ok(ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .message("Get product successfully!")
                .data(productService.findAllProducts())
                .code(HttpStatus.OK.value())
                .build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct (
            @RequestPart("product") @Valid ProductCreateRequest productCreateRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ProductResponse productResponse = productService.createProduct(productCreateRequest, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ProductResponse>builder()
                .success(true)
                .data(productResponse)
                .code(HttpStatus.CREATED.value())
                .message("Create new product successfully!")
                .build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct (
            @PathVariable Long id,
            @RequestPart("product") @Valid ProductUpdateRequest productUpdateRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        ProductResponse productResponse = productService.updateProduct(id, productUpdateRequest, image);
        return ResponseEntity.ok(ApiResponse.<ProductResponse>builder()
                .data(productResponse)
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Update product successfully!")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Delete product successfully!")
                .build());
    }
}
