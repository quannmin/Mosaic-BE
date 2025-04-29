package com.mosaic.domain.request;

import com.mosaic.util.constant.GenderEnum;
import com.mosaic.util.constant.RoleEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    String userName;
    String fullName;
    String email;
    String password;
    String phoneNumber;
    Date dob;
    RoleEnum role;
    GenderEnum gender;
}
