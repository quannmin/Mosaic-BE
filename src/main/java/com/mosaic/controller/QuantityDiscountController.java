package com.mosaic.controller;

import com.mosaic.domain.request.QuantityDiscountRequest;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.domain.response.QuantityDiscountResponse;
import com.mosaic.service.spec.QuantityDiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/quantity-discounts")
@RequiredArgsConstructor
public class QuantityDiscountController {
    private final QuantityDiscountService quantityDiscountService;

    @GetMapping("/product-variants/{variantId}")
    public ResponseEntity<ApiResponse<List<QuantityDiscountResponse>>> findDiscountsByVariant(
            @PathVariable Long variantId) {
        return ResponseEntity.ok(ApiResponse.<List<QuantityDiscountResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Retrieved quantity discounts successfully")
                .data(quantityDiscountService.findQuantityDiscountByProductVariantId(variantId))
                .success(true)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuantityDiscountResponse>> createDiscount(
            @RequestBody @Valid QuantityDiscountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<QuantityDiscountResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Quantity discount created successfully")
                        .data(quantityDiscountService.createQuantityDiscount(request))
                        .success(true)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuantityDiscountResponse>> updateDiscount(
            @PathVariable Long id,
            @RequestBody @Valid QuantityDiscountRequest request) {
        return ResponseEntity.ok(ApiResponse.<QuantityDiscountResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Quantity discount updated successfully")
                .data(quantityDiscountService.updateQuantityDiscount(id, request))
                .success(true)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDiscount(@PathVariable Long id) {
        quantityDiscountService.deleteQuantityDiscount(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Quantity discount deleted successfully")
                .success(true)
                .build());
    }
}
