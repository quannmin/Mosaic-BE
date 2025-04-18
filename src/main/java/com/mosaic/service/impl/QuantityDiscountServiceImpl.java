package com.mosaic.service.impl;

import com.mosaic.domain.request.QuantityDiscountRequest;
import com.mosaic.domain.response.QuantityDiscountResponse;
import com.mosaic.entity.ProductVariant;
import com.mosaic.entity.QuantityDiscount;
import com.mosaic.exception.DuplicateElementException;
import com.mosaic.exception.ElementNotFoundException;
import com.mosaic.mapper.QuantityDiscountMapper;
import com.mosaic.repository.QuantityDiscountRepository;
import com.mosaic.service.spec.ProductVariantService;
import com.mosaic.service.spec.QuantityDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuantityDiscountServiceImpl implements QuantityDiscountService {

    private final QuantityDiscountRepository quantityDiscountRepository;
    private final QuantityDiscountMapper quantityDiscountMapper;
    private final ProductVariantService productVariantService;

    @Override
    public QuantityDiscountResponse findQuantityDiscountById(Long id) {
        return quantityDiscountMapper.toQuantityDiscountResponse(
                quantityDiscountRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Not found quantity discount"))
        );
    }

    @Override
    public List<QuantityDiscountResponse> findQuantityDiscountByProductVariantId(Long productVariantId) {
        ProductVariant productVariant = productVariantService.findProductVariantById(productVariantId);

        return quantityDiscountRepository.findByProductVariant(productVariant)
                .stream().map(quantityDiscountMapper::toQuantityDiscountResponse).toList();
    }

    @Override
    @Transactional
    public QuantityDiscountResponse createQuantityDiscount(QuantityDiscountRequest request) {
        ProductVariant productVariant = productVariantService.findProductVariantById(request.getProductVariantId());
        if(quantityDiscountRepository.existsByProductVariantIdAndMinQuantity(
                request.getProductVariantId(), request.getMinQuantity()))
        {
            throw new DuplicateElementException("A discount for this quantity already exists for this product variant");
        }

        QuantityDiscount quantityDiscount = quantityDiscountMapper.toQuantityDiscount(request);
        quantityDiscount.setProductVariant(productVariant);
        return quantityDiscountMapper.toQuantityDiscountResponse(quantityDiscountRepository.save(quantityDiscount));
    }

    @Override
    @Transactional
    public QuantityDiscountResponse updateQuantityDiscount(Long id, QuantityDiscountRequest request) {
        QuantityDiscount discount = quantityDiscountRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Quantity discount not found"));

        ProductVariant variant = productVariantService.findProductVariantById(request.getProductVariantId());

        if (!discount.getProductVariant().getId().equals(request.getProductVariantId()) ||
                !discount.getMinQuantity().equals(request.getMinQuantity())) {
            if (quantityDiscountRepository.existsByProductVariantIdAndMinQuantity(
                    request.getProductVariantId(), request.getMinQuantity())) {
                throw new DuplicateElementException(
                        "A discount for this quantity already exists for this product variant");
            }
        }
        discount.setProductVariant(variant);
        discount.setMinQuantity(request.getMinQuantity());
        discount.setDiscountPercentage(request.getDiscountPercentage());

        return quantityDiscountMapper.toQuantityDiscountResponse(quantityDiscountRepository.save(discount));
    }

    @Override
    @Transactional
    public void deleteQuantityDiscount(Long id) {
        if (!quantityDiscountRepository.existsById(id)) {
            throw new ElementNotFoundException("Quantity discount not found");
        }
        quantityDiscountRepository.deleteById(id);
    }
}
