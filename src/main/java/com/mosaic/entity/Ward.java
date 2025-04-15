package com.mosaic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.List;

@Entity(name = "wards")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ward {
    @Id
    String code;
    String name;
    @OneToMany(mappedBy = "ward")
    List<Address> addresses;
    @ManyToOne(fetch = FetchType.LAZY)
    District district;
}
