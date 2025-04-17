package com.mosaic.controller;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.request.ProductVariantUpdateRequest;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.domain.response.ProductVariantResponse;
import com.mosaic.service.spec.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/product-variant")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductVariantResponse>> addProductVariant(
            @RequestPart("product-variant") ProductVariantCreateRequest productVariantCreateRequest,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ProductVariantResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Create product variant successfully!")
                        .data(productVariantService.createProductVariant(productVariantCreateRequest, images))
                        .success(true)
                        .build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductVariantResponse>> updateProductVariant(
            @PathVariable Long id,
            @RequestPart("product-variant") @Valid ProductVariantUpdateRequest productVariantUpdateRequest,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {

        return  ResponseEntity.ok(ApiResponse.<ProductVariantResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update product variant successfully!")
                .data(productVariantService.updateProductVariant(id, productVariantUpdateRequest, images))
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> findProductVariant(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.<ProductVariantResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message("Get product variant successfully!")
                        .success(true)
                        .data(productVariantService.findProductVariantById(id))
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> findAllProductVariants() {
        return ResponseEntity.ok(
                ApiResponse.<List<ProductVariantResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Get all product variants successfully!")
                        .success(true)
                        .data(productVariantService.findAllProductVariants())
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductVariant(@PathVariable Long id) {
        productVariantService.deleteProductVariant(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(HttpStatus.OK.value())
                        .message("Delete product variants successfully!")
                        .success(true)
                        .build());
    }
}
