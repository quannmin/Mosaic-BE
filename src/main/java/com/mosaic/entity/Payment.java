package com.mosaic.entity;

import com.mosaic.util.constant.PaymentMethodEnum;
import com.mosaic.util.constant.PaymentStatusEnum;
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

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String referenceCode;
    PaymentStatusEnum status;
    PaymentMethodEnum paymentMethod;
    BigDecimal amount;
    @CreatedDate
    Instant createdAt;
    @LastModifiedDate
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}
