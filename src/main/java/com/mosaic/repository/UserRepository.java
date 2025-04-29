package com.mosaic.repository;

import com.mosaic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUerByEmailOrUserNameOrPhoneNumber(String email, String username, String phoneNumber);
}
