package com.mosaic.repository;

import com.mosaic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM users u WHERE u.email = ?1 OR u.userName = ?2 OR u.phoneNumber = ?3")
    Optional<User> findUerByEmailOrUserNameOrPhoneNumber(String email, String userName, String phoneNumber);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByPhoneNumber(String phoneNumber);
}
