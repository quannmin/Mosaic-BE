package com.mosaic.service.impl;

import com.mosaic.domain.request.Authentication.PasswordRequests;
import com.mosaic.domain.request.Authentication.RegisterRequest;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.DuplicateResourceException;
import com.mosaic.mapper.UserMapper;
import com.mosaic.repository.UserRepository;
import com.mosaic.service.spec.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String PREFIX = "m_";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int RANDOM_LENGTH = 10;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        if (registerRequest.getPhoneNumber() != null && !registerRequest.getPhoneNumber().isEmpty() &&
                userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new DuplicateResourceException("User", "Phone number", registerRequest.getPhoneNumber());
        }
        User user = new User();
        user.setUserName(generateUniqueUsername());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    private String generateUniqueUsername() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    @Override
    public void forgotPassword(PasswordRequests.ForgotPasswordRequest request) {

    }

    @Override
    public void resetPassword(PasswordRequests.ResetPasswordRequest request) {

    }

    @Override
    public void changePassword(Long userId, PasswordRequests.ChangePasswordRequest request) {

    }

    @Override
    public void logout(String refreshToken) {

    }
}
