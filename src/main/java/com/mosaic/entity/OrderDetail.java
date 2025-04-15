package com.mosaic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer quantity;
    BigDecimal unit_price;
    @ManyToOne
    Order order;
    @ManyToOne
    ProductVariant productVariant;
}
