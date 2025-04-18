package com.mosaic.mapper;

import com.mosaic.domain.request.QuantityDiscountRequest;
import com.mosaic.domain.response.QuantityDiscountResponse;
import com.mosaic.entity.QuantityDiscount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuantityDiscountMapper {
    QuantityDiscount toQuantityDiscount(QuantityDiscountRequest request);
    QuantityDiscountResponse toQuantityDiscountResponse(QuantityDiscount discount);
}
