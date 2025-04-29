package com.mosaic.service.spec;

import com.mosaic.entity.User;

public interface UserService {
     User findUserById(Long id);
     User findUerByEmailOrUsernameOrPhoneNumber(String input);
}
