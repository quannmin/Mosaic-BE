package com.mosaic.entity;

import com.mosaic.util.constant.OrderStatusEnum;
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

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal totalPrice;
    String referenceCode;
    @Enumerated(EnumType.STRING)
    OrderStatusEnum status;
    @CreatedDate
    Instant createdAt;
    @LastModifiedDate
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    Address address;
    @OneToMany(mappedBy = "order")
    List<OrderDetail> details;
}
