package com.mosaic.entity;

import com.mosaic.util.constant.OtpEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity(name = "otp_codes")
@EntityListeners(AuditingEntityListener.class)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String otp;

    @Column(nullable = false)
    Instant expiryDate;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private int attempts;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpEnum type;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public void incrementAttempts() {
        this.attempts++;
    }
}
