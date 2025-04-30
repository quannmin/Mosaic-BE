package com.mosaic.mapper;

import com.mosaic.domain.request.UserCreateRequest;
import com.mosaic.domain.request.UserUpdateRequest;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest userCreateRequest);

    UserResponse toUserResponse(User user);

    void updateUserFromRequest(UserUpdateRequest updateRequest, @MappingTarget User user);
}
