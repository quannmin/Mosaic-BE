package com.mosaic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.List;

@Entity(name = "provinces")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Province {
    @Id
    String code;
    String name;
    @OneToMany(mappedBy = "province")
    List<Address> addresses;
    @OneToMany(mappedBy = "province")
    List<District> districts;
}
