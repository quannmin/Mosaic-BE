package com.mosaic.service.spec;

import com.mosaic.domain.request.Authentication.PasswordRequests;
import com.mosaic.domain.request.Authentication.RegisterRequest;
import com.mosaic.domain.request.Authentication.VerificationRequests;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
    String generateToken(Authentication authentication);
    String generateToken(User user);
    UserResponse register(RegisterRequest registerRequest);
    boolean forgotPassword(PasswordRequests.ForgotPasswordRequest request);
    boolean verifyOtp(VerificationRequests.VerifyOtpRequest request);
    boolean resetPassword(PasswordRequests.ResetPasswordRequest request);
    boolean  changePassword(Long userId, PasswordRequests.ChangePasswordRequest request);
    void logout(String refreshToken);
}
