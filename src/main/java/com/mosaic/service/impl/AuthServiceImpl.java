package com.mosaic.service.impl;

import com.mosaic.domain.request.Authentication.PasswordRequests;
import com.mosaic.domain.request.Authentication.RegisterRequest;
import com.mosaic.domain.request.Authentication.VerificationRequests;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.BadRequestException;
import com.mosaic.exception.custom.DuplicateResourceException;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.exception.custom.TokenRefreshException;
import com.mosaic.mapper.UserMapper;
import com.mosaic.repository.UserRepository;
import com.mosaic.service.security.DomainUserDetailsService;
import com.mosaic.service.spec.AuthService;
import com.mosaic.service.spec.EmailService;
import com.mosaic.service.spec.OtpService;
import com.mosaic.service.spec.RefreshTokenService;
import com.mosaic.util.constant.OtpEnum;
import com.mosaic.util.constant.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${app.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    private static final String PREFIX = "m_";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int RANDOM_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;
    private final JwtEncoder jwtEncoder;


    @Override
    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        Instant validity = Instant.now().plus(tokenValidityInSeconds, ChronoUnit.SECONDS);
        DomainUserDetailsService.CustomUserDetails customUserDetails =
                (DomainUserDetailsService.CustomUserDetails) authentication.getPrincipal();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .claim("userId", customUserDetails.getUserId())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet)).getTokenValue();
    }

    @Override
    public String generateToken(User user) {
        Instant validity = Instant.now().plus(tokenValidityInSeconds, ChronoUnit.SECONDS);

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        String authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(validity)
                .subject(user.getUserName() != null ? user.getUserName() : user.getPhoneNumber())
                .claim("authorities", authorities)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet)).getTokenValue();
    }

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
        user.setRole(RoleEnum.CUSTOMER);
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

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is not correct!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException("Refresh token cannot be empty!");
        }

        if (!refreshTokenService.isTokenValid(refreshToken)) {
            throw new TokenRefreshException(refreshToken, "Refresh token is invalid or has been revoked!");
        }

        refreshTokenService.revokeToken(refreshToken);
    }
}
