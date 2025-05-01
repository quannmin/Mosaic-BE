package com.mosaic.domain.request.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
public class PasswordRequests {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ForgotPasswordRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ResetPasswordRequest {
        @NotBlank(message = "Token is required")
        String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String newPassword;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ChangePasswordRequest {
        @NotBlank(message = "Current password is required")
        String currentPassword;

        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String newPassword;
    }
}
