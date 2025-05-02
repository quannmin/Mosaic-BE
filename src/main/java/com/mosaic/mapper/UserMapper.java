package com.mosaic.mapper;

import com.mosaic.domain.request.UserCreateRequest;
import com.mosaic.domain.request.UserUpdateRequest;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    User toUser(UserCreateRequest userCreateRequest);

    @Mapping(source = "activated", target = "activated")
    UserResponse toUserResponse(User user);

    void updateUserFromRequest(UserUpdateRequest updateRequest, @MappingTarget User user);
}
