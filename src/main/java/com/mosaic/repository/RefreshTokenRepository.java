package com.mosaic.repository;

import com.mosaic.entity.RefreshToken;
import com.mosaic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken findByUser(User user);
    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.token = ?1")
    void revokeToken(String token);
    boolean existsByTokenAndRevokedFalse(String token);
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.token = ?1")
    void deleteByToken(String token);
}
