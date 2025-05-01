package com.mosaic.service.spec;

import com.mosaic.entity.RefreshToken;
import com.mosaic.exception.custom.TokenRefreshException;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException;

    Optional<RefreshToken> findByToken(String token);

    void revokeToken(String token);

    boolean isTokenValid(String token);
}
