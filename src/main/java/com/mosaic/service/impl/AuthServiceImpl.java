package com.mosaic.service.impl;

import com.mosaic.domain.request.Authentication.PasswordRequests;
import com.mosaic.domain.request.Authentication.RegisterRequest;
import com.mosaic.domain.request.Authentication.VerificationRequests;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.BadRequestException;
import com.mosaic.exception.custom.DuplicateResourceException;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.mapper.UserMapper;
import com.mosaic.repository.UserRepository;
import com.mosaic.service.spec.AuthService;
import com.mosaic.service.spec.EmailService;
import com.mosaic.service.spec.OtpService;
import com.mosaic.util.constant.OtpEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String PREFIX = "m_";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int RANDOM_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OtpService otpService;


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
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public boolean forgotPassword(PasswordRequests.ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        if(!user.isActivated()) {
            return false;
        }

        String otp = otpService.generateOtp(request.getEmail(), OtpEnum.PASSWORD_RESET);

        emailService.sendPasswordResetOtp(request.getEmail(), otp);

        return true;
    }

    @Override
    public boolean verifyOtp(VerificationRequests.VerifyOtpRequest request) {
        return otpService.verifyOtpByEmail(request.getEmail(), request.getOtp(), OtpEnum.PASSWORD_RESET);
    }

    @Override
    public boolean resetPassword(PasswordRequests.ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean changePassword(Long userId, PasswordRequests.ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        if(!passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is not correct!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void logout(String refreshToken) {

    }
}
