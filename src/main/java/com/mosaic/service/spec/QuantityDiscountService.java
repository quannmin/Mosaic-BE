package com.mosaic.service.spec;

import com.mosaic.domain.request.QuantityDiscountRequest;
import com.mosaic.domain.response.QuantityDiscountResponse;

import java.util.List;

public interface QuantityDiscountService {
    public QuantityDiscountResponse findQuantityDiscountById(Long id);
    public List<QuantityDiscountResponse> findQuantityDiscountByProductVariantId(Long productVariantId);
    public QuantityDiscountResponse createQuantityDiscount(QuantityDiscountRequest request);
    public QuantityDiscountResponse updateQuantityDiscount(Long id, QuantityDiscountRequest request);
    public void deleteQuantityDiscount(Long id);
}
