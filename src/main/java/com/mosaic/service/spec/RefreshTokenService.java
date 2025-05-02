package com.mosaic.service.spec;

import com.mosaic.entity.RefreshToken;
import com.mosaic.exception.custom.TokenRefreshException;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId, long refreshTokenDuration);

    boolean verifyExpiration(RefreshToken token) throws TokenRefreshException;

    RefreshToken findByToken(String token);

    void revokeToken(String token);

    boolean isTokenValid(String token);

    RefreshToken updateExpiryDate(RefreshToken refreshToken, long refreshTokenDuration);

    void deleteByToken(String token);
}
