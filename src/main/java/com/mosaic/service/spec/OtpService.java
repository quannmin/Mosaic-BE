package com.mosaic.service.spec;

import com.mosaic.util.constant.OtpEnum;

public interface OtpService {
    public String generateOtp(String email, OtpEnum otpEnum);
    boolean verifyOtpByEmail(String email, String otp, OtpEnum otpEnum);
    void cleanupExpiredOtps();
}
