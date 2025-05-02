package com.mosaic.service.impl;

import com.mosaic.entity.RefreshToken;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.repository.RefreshTokenRepository;
import com.mosaic.repository.UserRepository;
import com.mosaic.service.spec.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;


    @Override
    public RefreshToken createRefreshToken(Long userId, long refreshTokenDuration) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId.toString()));
        RefreshToken existingToken = refreshTokenRepository.findByUser(user);
        if (existingToken != null) {
            String token = UUID.randomUUID().toString();
            existingToken.setToken(token);
            existingToken.setExpiryDate(Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS));
            existingToken.setRevoked(false);
            return refreshTokenRepository.save(existingToken);
        } else {
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS))
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }
    }

    @Override
    public boolean verifyExpiration(RefreshToken token) {
        if(token.isRevoked()){
            return false;
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return false;
        }
        return true;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new ResourceNotFoundException("Refresh token", "token", token));
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

    @Override
    public RefreshToken updateExpiryDate(RefreshToken refreshToken, long refreshTokenDuration) {
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
