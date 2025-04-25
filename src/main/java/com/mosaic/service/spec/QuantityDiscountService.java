package com.mosaic.service.spec;

import com.mosaic.domain.request.QuantityDiscountRequest;
import com.mosaic.domain.response.QuantityDiscountResponse;

import java.util.List;

public interface QuantityDiscountService {
     QuantityDiscountResponse findQuantityDiscountById(Long id);
     List<QuantityDiscountResponse> findQuantityDiscountByProductVariantId(Long productVariantId);
     QuantityDiscountResponse createQuantityDiscount(QuantityDiscountRequest request);
     QuantityDiscountResponse updateQuantityDiscount(Long id, QuantityDiscountRequest request);
     void deleteQuantityDiscount(Long id);
}
