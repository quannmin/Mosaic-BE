package com.mosaic.service.spec;

import com.mosaic.domain.response.localtion.DistrictDto;
import com.mosaic.domain.response.localtion.ProvinceDto;
import com.mosaic.domain.response.localtion.WardDto;
import com.mosaic.domain.request.AddressRequest;
import com.mosaic.domain.response.AddressResponse;
import com.mosaic.entity.Address;
import com.mosaic.entity.District;
import com.mosaic.entity.Province;
import com.mosaic.entity.Ward;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(AddressRequest addressRequest);
    AddressResponse updateAddress(Long id, AddressRequest addressRequest);
    Address findAddressById(Long id);
    AddressResponse findAddressResponseById(Long id);
    List<AddressResponse> findAllAddresses();
    List<AddressResponse> findAddressesByUserId(Long userId);
    void deleteAddressById(Long id);
    void fetchAndSaveLocationData();
    List<ProvinceDto> findAllProvinces();
    ProvinceDto findProvinceDtoById(String provinceCode);
    Province findProvinceById(String provinceCode);
    List<DistrictDto> findAllDistricts();
    List<DistrictDto> findDistrictsByProvinceCode(String provinceCode);
    DistrictDto findDistrictDtoById(String districtCode);
    District findDistrictById(String districtCode);
    List<WardDto> findAllWards();
    List<WardDto> findWardsByDistrictCode(String districtCode);
    WardDto findWardDtoById(String wardCode);
    Ward findWardById(String wardCode);
}
