package com.mosaic.mapper;

import com.mosaic.domain.request.AddressRequest;
import com.mosaic.domain.response.AddressResponse;
import com.mosaic.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest addressCreateRequest);

    @Mapping(target = "provinceCode", source = "province.code")
    @Mapping(target = "districtCode", source = "district.code")
    @Mapping(target = "wardCode", source = "ward.code")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "isDefault", source = "default")
    AddressResponse toAddressResponse(Address address);
    List<AddressResponse> toAddressResponseList(List<Address> addressList);
    void toAddressUpdate(AddressRequest addressCreateRequest, @MappingTarget Address address);
}
