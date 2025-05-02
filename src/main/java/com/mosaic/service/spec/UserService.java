package com.mosaic.service.spec;

import com.mosaic.domain.request.UserCreateRequest;
import com.mosaic.domain.request.UserUpdateRequest;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;

import java.util.List;

public interface UserService {
     UserResponse createUser(UserCreateRequest userCreateRequest);

     UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest);

     UserResponse findUserResponseById(Long id);

     List<UserResponse> getAllUsers();

     void deleteUser(Long id);

     User findUserById(Long id);

     User findByUsername(String username);

     User findUerByEmailOrUsernameOrPhoneNumber(String input);
}
