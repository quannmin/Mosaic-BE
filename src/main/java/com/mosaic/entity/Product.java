package com.mosaic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    BigDecimal basePrice;
    BigDecimal discountPrice;
    String mainImageUrl;
    String description;
    @OneToMany(mappedBy = "product")
    List<ProductVariant> productVariants;
}
