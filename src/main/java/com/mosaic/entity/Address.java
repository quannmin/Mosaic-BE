package com.mosaic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity(name = "addresses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fullName;
    String phoneNumber;
    String specificAddress;
    String addressType;
    boolean isDefault;
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    Province province;
    @ManyToOne(fetch = FetchType.LAZY)
    District district;
    @ManyToOne(fetch = FetchType.LAZY)
    Ward ward;
    @OneToMany(mappedBy = "address")
    List<Order> orders;
}
