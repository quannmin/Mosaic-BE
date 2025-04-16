package com.mosaic.controller;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.domain.response.ProductVariantResponse;
import com.mosaic.service.spec.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
