package com.mosaic.repository;

import com.mosaic.entity.ProductVariant;
import com.mosaic.entity.QuantityDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityDiscountRepository extends JpaRepository<QuantityDiscount, Long> {
    List<QuantityDiscount> findByProductVariant(ProductVariant productVariant);
    List<QuantityDiscount> findByProductVariantIdOrderByMinQuantityDesc(Long productVariantId);
    boolean existsByProductVariantIdAndMinQuantity(Long productVariantId, Integer minQuantity);
}
