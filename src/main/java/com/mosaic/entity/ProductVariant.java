package com.mosaic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity(name = "product_variants")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    BigDecimal price;
    String color;
    String sizeDescription;
    Integer stockQuantity;
    @CreatedDate
    Instant createdAt;
    @LastModifiedDate
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    @OneToMany(mappedBy = "productVariant")
    List<Image> images;
    @OneToMany(mappedBy = "productVariant")
    List<OrderDetail> orderDetails;
}
