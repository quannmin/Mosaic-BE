package com.mosaic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String urlDownload;
    Integer display_order;
    @ManyToOne(fetch = FetchType.LAZY)
    ProductVariant productVariant;
}
