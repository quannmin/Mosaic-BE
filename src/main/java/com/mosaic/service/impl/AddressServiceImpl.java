package com.mosaic.service.impl;

import com.mosaic.domain.response.localtion.DistrictDto;
import com.mosaic.domain.response.localtion.ProvinceDto;
import com.mosaic.domain.response.localtion.WardDto;
import com.mosaic.domain.request.AddressRequest;
import com.mosaic.domain.response.AddressResponse;
import com.mosaic.entity.*;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.mapper.AddressMapper;
import com.mosaic.repository.AddressRepository;
import com.mosaic.repository.DistrictRepository;
import com.mosaic.repository.ProvinceRepository;
import com.mosaic.repository.WardRepository;
import com.mosaic.service.spec.AddressService;
import com.mosaic.service.spec.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final RestTemplate restTemplate;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final AddressMapper addressMapper;
    private final UserService userService;
    private final AddressRepository addressRepository;


    @Override
    public AddressResponse createAddress(AddressRequest addressRequest) {
//        User user = userService.findUserById(addressRequest.getUserId());
        Province province = findProvinceById(addressRequest.getProvinceCode());
        District district = findDistrictById(addressRequest.getDistrictCode());
        Ward ward = findWardById(addressRequest.getWardCode());

        Address address = addressMapper.toAddress(addressRequest);
//        address.setUser(user);
        address.setProvince(province);
        address.setDistrict(district);
        address.setWard(ward);

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    public AddressResponse updateAddress(Long id, AddressRequest addressRequest) {
        Address address = findAddressById(id);
        User user = userService.findUserById(addressRequest.getUserId());
        Province province = findProvinceById(addressRequest.getProvinceCode());
        District district = findDistrictById(addressRequest.getDistrictCode());
        Ward ward = findWardById(addressRequest.getWardCode());
        addressMapper.toAddressUpdate(addressRequest, address);
        address.setUser(user);
        address.setProvince(province);
        address.setDistrict(district);
        address.setWard(ward);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    public Address findAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
    }

    @Override
    public AddressResponse findAddressResponseById(Long id) {
        Address address = findAddressById(id);
        return addressMapper.toAddressResponse(address);
    }

    @Override
    public List<AddressResponse> findAllAddresses() {
        return addressMapper.toAddressResponseList(addressRepository.findAll());
    }

    @Override
    public List<AddressResponse> findAddressesByUserId(Long userId) {
        return addressMapper.toAddressResponseList(addressRepository.findAllByUserId(userId));
    }

    @Override
    public void deleteAddressById(Long id) {
        addressRepository.deleteById(id);
    }

    @Transactional
    public void fetchAndSaveLocationData() {
        String apiUrl = "https://provinces.open-api.vn/api/?depth=3";
        ResponseEntity<ProvinceDto[]> response = restTemplate.getForEntity(apiUrl, ProvinceDto[].class);
        if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            ProvinceDto[] provinceResponse = response.getBody();
            List<Ward> wards = new ArrayList<>();

            for (ProvinceDto province : provinceResponse) {

                Province provinceEntity = new Province();
                provinceEntity.setCode(province.getCode());
                provinceEntity.setName(province.getName());
                provinceEntity.setDivisionType(province.getDivision_type());
                provinceEntity.setCreatedBy("system");
                provinceEntity.setUpdatedBy("system");
                Province savedProvince = provinceRepository.save(provinceEntity);

                if (!province.getDistricts().isEmpty()) {
                    for (DistrictDto district : province.getDistricts()) {

                        District districtEntity = new District();
                        districtEntity.setCode(district.getCode());
                        districtEntity.setName(district.getName());
                        districtEntity.setDivisionType(district.getDivision_type());
                        districtEntity.setProvince(savedProvince);
                        districtEntity.setCreatedBy("system");
                        districtEntity.setUpdatedBy("system");
                        District savedDistrict = districtRepository.save(districtEntity);

                        if (!district.getWards().isEmpty()) {
                            for (WardDto ward : district.getWards()) {
                                Ward wardEntity = new Ward();
                                wardEntity.setCode(ward.getCode());
                                wardEntity.setName(ward.getName());
                                wardEntity.setDivisionType(ward.getDivision_type());
                                wardEntity.setDistrict(savedDistrict);
                                wardEntity.setCreatedBy("system");
                                wardEntity.setUpdatedBy("system");
                                wards.add(wardEntity);
                            }
                        }
                    }
                }
            }
            wardRepository.saveAll(wards);
        }
    }

    @Override
    public List<ProvinceDto> findAllProvinces() {
        return provinceRepository.findAll().stream().map(province -> {
            ProvinceDto provinceDto = new ProvinceDto();
            provinceDto.setCode(province.getCode());
            provinceDto.setName(province.getName());
            provinceDto.setDivision_type(province.getDivisionType());
            return provinceDto;
        }).toList();
    }

    @Override
    public Province findProvinceById(String provinceCode) {
        return provinceRepository.findById(provinceCode).orElseThrow(() -> new ResourceNotFoundException(
                "Province", "provinceCode", provinceCode));
    }

    @Override
    public ProvinceDto findProvinceDtoById(String provinceCode) {
        Province province = findProvinceById(provinceCode);
        ProvinceDto provinceDto = new ProvinceDto();
        provinceDto.setCode(province.getCode());
        provinceDto.setName(province.getName());
        provinceDto.setDivision_type(province.getDivisionType());
        return provinceDto;
    }

    @Override
    public List<DistrictDto> findAllDistricts() {
        return districtRepository.findAll().stream().map(district -> {
            DistrictDto districtDto = new DistrictDto();
            districtDto.setCode(district.getCode());
            districtDto.setName(district.getName());
            districtDto.setDivision_type(district.getDivisionType());
            return districtDto;
        }).toList();
    }

    @Override
    public List<DistrictDto> findDistrictsByProvinceCode(String provinceCode) {
        return districtRepository.findAllByProvinceCode(provinceCode).stream().map(district -> {
            DistrictDto districtDto = new DistrictDto();
            districtDto.setCode(district.getCode());
            districtDto.setName(district.getName());
            districtDto.setDivision_type(district.getDivisionType());
            return districtDto;
        }).toList();
    }

    @Override
    public District findDistrictById(String districtCode) {
        return districtRepository.findById(districtCode).orElseThrow(() -> new ResourceNotFoundException(
                "District", "districtCode", districtCode));
    }

    @Override
    public DistrictDto findDistrictDtoById(String districtCode) {
        District district = findDistrictById(districtCode);
        DistrictDto districtDto = new DistrictDto();
        districtDto.setCode(district.getCode());
        districtDto.setName(district.getName());
        districtDto.setDivision_type(district.getDivisionType());
        return districtDto;
    }

    @Override
    public List<WardDto> findAllWards() {
        return wardRepository.findAll().stream().map(ward -> {
            WardDto wardDto = new WardDto();
            wardDto.setCode(ward.getCode());
            wardDto.setName(ward.getName());
            wardDto.setDivision_type(ward.getDivisionType());
            return wardDto;
        }).toList();
    }

    @Override
    public List<WardDto> findWardsByDistrictCode(String districtCode) {
        return wardRepository.findAllByDistrictCode(districtCode).stream().map(ward -> {
            WardDto wardDto = new WardDto();
            wardDto.setCode(ward.getCode());
            wardDto.setName(ward.getName());
            wardDto.setDivision_type(ward.getDivisionType());
            return wardDto;
        }).toList();
    }

    @Override
    public Ward findWardById(String wardCode) {
        return wardRepository.findById(wardCode).orElseThrow(() -> new ResourceNotFoundException(
                "Ward", "wardCode", wardCode));
    }

    @Override
    public WardDto findWardDtoById(String wardCode) {
        Ward ward = findWardById(wardCode);

        WardDto wardDto = new WardDto();
        wardDto.setCode(ward.getCode());
        wardDto.setName(ward.getName());
        wardDto.setDivision_type(ward.getDivisionType());
        return wardDto;
    }
}
