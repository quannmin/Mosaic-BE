package com.mosaic.domain.request.Authentication;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public class VerificationRequests {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class VerifyEmailRequest {
        @NotBlank(message = "Token is required")
        String token;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class VerifyOtpRequest {
        @Email(message = "Invalid email format")
        String email;

        @Pattern(regexp = "^[0-9]{10,12}$", message = "Phone number must be 10-12 digits")
        String phoneNumber;

        @NotBlank(message = "OTP is required")
        @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits")
        String otp;
    }
}
