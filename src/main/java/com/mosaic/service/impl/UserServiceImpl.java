package com.mosaic.service.impl;

import com.mosaic.entity.User;
import com.mosaic.exception.ElementNotFoundException;
import com.mosaic.repository.UserRepository;
import com.mosaic.service.spec.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("User not found!"));
    }
}
