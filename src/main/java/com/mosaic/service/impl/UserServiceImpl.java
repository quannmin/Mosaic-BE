package com.mosaic.service.impl;

import com.mosaic.domain.request.UserCreateRequest;
import com.mosaic.domain.request.UserUpdateRequest;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.BadRequestException;
import com.mosaic.exception.custom.DuplicateResourceException;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.mapper.UserMapper;
import com.mosaic.repository.UserRepository;
import com.mosaic.service.spec.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        if(userRepository.existsByUserName(userCreateRequest.getUserName())) {
            throw new DuplicateResourceException("User", "Username", userCreateRequest.getUserName());
        }
        if(userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new DuplicateResourceException("User", "Email", userCreateRequest.getEmail());
        }
        if(userRepository.existsByPhoneNumber(userCreateRequest.getPhoneNumber())) {
            throw new DuplicateResourceException("User", "Phone number", userCreateRequest.getPhoneNumber());
        }

        User user = userMapper.toUser(userCreateRequest);

        if(userCreateRequest.getUserName().isEmpty()) {
            String baseUsername = user.getEmail().split("@")[0];
            String generatedUsername = generateUniqueUsername(baseUsername);
            user.setUserName(generatedUsername);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    private String generateUniqueUsername(String baseUsername) {
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUserName(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User user = findUserById(id);
        if (userUpdateRequest.getEmail() != null &&
                !user.getEmail().equals(userUpdateRequest.getEmail()) &&
                userRepository.existsByEmail(userUpdateRequest.getEmail()))
        {
            throw new DuplicateResourceException("User", "Email", userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getPhoneNumber() != null &&
                !user.getPhoneNumber().equals(userUpdateRequest.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userUpdateRequest.getPhoneNumber())) {
            throw new DuplicateResourceException("User", "Phone number", userUpdateRequest.getPhoneNumber());
        }

        if (userUpdateRequest.getUserName() != null && !userUpdateRequest.getUserName().isEmpty()) {
            if (user.isUsernameUpdated()) {
                throw new BadRequestException("Username can only be updated once");
            }
            if (!user.getUserName().equals(userUpdateRequest.getUserName()) &&
                    userRepository.existsByUserName(userUpdateRequest.getUserName())) {
                throw new BadRequestException("Username already in use");
            }
            user.setUserName(userUpdateRequest.getUserName());
            user.setUsernameUpdated(true);
            userUpdateRequest.setUserName(null);
        }

        userMapper.updateUserFromRequest(userUpdateRequest, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse findUserResponseById(Long id) {
        User user = findUserById(id);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
    }

    @Override
    public User findUerByEmailOrUsernameOrPhoneNumber(String input) {
        return userRepository.findUerByEmailOrUserNameOrPhoneNumber(input,input,input)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email/phone/user-name", input));
    }
}
