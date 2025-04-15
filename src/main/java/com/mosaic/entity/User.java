package com.mosaic.entity;

import com.mosaic.util.constant.GenderEnum;
import com.mosaic.util.constant.RoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userName;
    String fullName;
    String email;
    String password;
    String phoneNumber;
    Date dob;
    @Enumerated(EnumType.STRING)
    RoleEnum role;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    Instant createdAt;
    @LastModifiedDate
    @Column(updatable = false)
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    @OneToMany(mappedBy = "user")
    List<Address> addresses;
    @OneToMany(mappedBy = "user")
    List<Order> orders;
    @OneToMany(mappedBy = "user")
    List<Payment> payments;
}
