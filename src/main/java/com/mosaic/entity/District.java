package com.mosaic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity(name = "districts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class District {
    @Id
    String code;
    String name;
    @OneToMany(mappedBy = "district")
    List<Address> addresses;
    @OneToMany(mappedBy = "district")
    List<Ward> wards;
    @ManyToOne(fetch = FetchType.LAZY)
    Province province;
}
