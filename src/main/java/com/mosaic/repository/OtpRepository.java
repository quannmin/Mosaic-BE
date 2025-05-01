package com.mosaic.repository;

import com.mosaic.entity.Otp;
import com.mosaic.util.constant.OtpEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findFirstByEmailAndTypeOrderByCreatedAtDesc(String email, OtpEnum type);
    Optional<Otp> findByEmailAndOtpAndType(String email, String otp, OtpEnum type);

    @Modifying
    @Query("UPDATE otp_codes o SET o.used = true WHERE o.email = ?1 AND o.type = ?2 AND o.used = false AND o.id = ?3")
    void invalidateOtherOtpsByEmailAndType(String email, OtpEnum type, Long currentOtpId);

    @Modifying
    @Query("DELETE FROM otp_codes o WHERE o.expiryDate < ?1")
    void deleteAllExpiredOtps(Instant now);
}


