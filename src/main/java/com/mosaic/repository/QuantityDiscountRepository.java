package com.mosaic.repository;

import com.mosaic.entity.ProductVariant;
import com.mosaic.entity.QuantityDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuantityDiscountRepository extends JpaRepository<QuantityDiscount, Long> {
    List<QuantityDiscount> findByProductVariant(ProductVariant productVariant);
    List<QuantityDiscount> findByProductVariantIdOrderByMinQuantityDesc(Long productVariantId);
    boolean existsByProductVariantIdAndMinQuantity(Long productVariantId, Integer minQuantity);
}
