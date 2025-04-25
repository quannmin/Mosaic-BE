package com.mosaic.domain.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    Long id;
    String fullName;
    String phoneNumber;
    String specificAddress;
    String addressType;
    String provinceCode;
    String districtCode;
    String wardCode;
    Long userId;
    boolean isDefault;
}
