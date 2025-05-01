package com.mosaic.service.impl;

import com.mosaic.entity.Otp;
import com.mosaic.repository.OtpRepository;
import com.mosaic.service.spec.OtpService;
import com.mosaic.util.constant.OtpEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final Random random = new Random();

    @Value("${app.otp.expiry-minutes}")
    private int otpExpiryMinutes;

    @Value("${app.otp.max-attempts}")
    private int maxAttempts;

    @Override
    public String generateOtp(String email, OtpEnum otpEnum) {
        String otp = generateOtpCode();

        Instant expiryDate = Instant.now().plusSeconds(otpExpiryMinutes * 60L);

        Otp otpEntity = Otp.builder()
                .email(email)
                .otp(otp)
                .expiryDate(expiryDate)
                .attempts(0)
                .type(otpEnum)
                .build();
        Otp savedOtp = otpRepository.save(otpEntity);
        otpRepository.invalidateOtherOtpsByEmailAndType(email, otpEnum, savedOtp.getId());
        return otp;
    }

    @Override
    public boolean verifyOtpByEmail(String email, String otp, OtpEnum otpEnum) {
        Optional<Otp> newestOtp = otpRepository.findFirstByEmailAndTypeOrderByCreatedAtDesc(email, otpEnum);

        if (newestOtp.isEmpty()) {
            return false;
        }

        Otp otpEntity = newestOtp.get();
        if(otpEntity.isUsed()) {
            return false;
        }

        if(otpEntity.isExpired()) {
            otpEntity.setUsed(true);
            otpRepository.save(otpEntity);
            return false;
        }

        otpEntity.incrementAttempts();

        if(otpEntity.getAttempts() > maxAttempts) {
            otpEntity.setUsed(true);
            otpRepository.save(otpEntity);
            return false;
        }

        boolean isValid = otpEntity.getOtp().equals(otp);
        if(isValid) {
            otpEntity.setUsed(true);
        }

        otpRepository.save(otpEntity);
        return isValid;
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    @Override
    public void cleanupExpiredOtps() {
        otpRepository.deleteAllExpiredOtps(Instant.now());
    }

    private String generateOtpCode() {
        StringBuilder otp = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
