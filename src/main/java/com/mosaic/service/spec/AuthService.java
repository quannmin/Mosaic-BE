package com.mosaic.service.spec;

import com.mosaic.domain.request.Authentication.PasswordRequests;
import com.mosaic.domain.request.Authentication.RegisterRequest;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    void forgotPassword(PasswordRequests.ForgotPasswordRequest request);
    void resetPassword(PasswordRequests.ResetPasswordRequest request);
    void changePassword(Long userId, PasswordRequests.ChangePasswordRequest request);
    void logout(String refreshToken);
}
