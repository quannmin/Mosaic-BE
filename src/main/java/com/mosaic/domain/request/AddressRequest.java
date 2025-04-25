package com.mosaic.domain.request;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    String fullName;
    String phoneNumber;
    String specificAddress;
    String addressType;
    String provinceCode;
    String districtCode;
    String wardCode;
    @Nullable
    Long userId;
    boolean isDefault;
}
