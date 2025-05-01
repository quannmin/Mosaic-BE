package com.mosaic.service.impl;

import com.mosaic.entity.RefreshToken;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.exception.custom.TokenRefreshException;
import com.mosaic.repository.RefreshTokenRepository;
import com.mosaic.repository.UserRepository;
import com.mosaic.service.spec.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${app.security.authentication.jwt.refresh-token.token-validity-in-seconds}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;


    @Override
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId.toString()));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.isRevoked()){
            throw new TokenRefreshException(token.getToken(), "Refresh token has been revoked");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token has expired");
        }

        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.revokeToken(token);
    }

    @Override
    public boolean isTokenValid(String token) {
        return refreshTokenRepository.existsByTokenAndRevokedFalse(token);
    }
}
