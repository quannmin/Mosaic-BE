package com.mosaic.controller;

import com.mosaic.domain.localtion.DistrictDto;
import com.mosaic.domain.localtion.ProvinceDto;
import com.mosaic.domain.localtion.WardDto;
import com.mosaic.domain.request.AddressRequest;
import com.mosaic.domain.response.AddressResponse;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.service.spec.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/fetch-data")
    public ResponseEntity<ApiResponse<Void>> fetchAndSaveLocationData() {
        addressService.fetchAndSaveLocationData();
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch data and save location data successfully!")
                .data(null)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(@Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .code(HttpStatus.CREATED.value())
                        .data(addressService.createAddress(addressRequest))
                        .message("Create address successfully!")
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(@PathVariable Long id,
                                                                      @Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .code(HttpStatus.OK.value())
                        .data(addressService.updateAddress(id, addressRequest))
                        .message("Update address successfully!")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(null)
                .message("Delete address successfully!")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<AddressResponse>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(addressService.findAddressResponseById(id))
                .message("Fetch address by id successfully!")
                .build());
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses() {
        return ResponseEntity.ok(ApiResponse.<List<AddressResponse>>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(addressService.findAllAddresses())
                .message("Fetch all addresses successfully!")
                .build());
    }

    @GetMapping("/users/userId")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.<List<AddressResponse>>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(addressService.findAddressesByUserId(userId))
                .message("Fetch all addresses by user id successfully!")
                .build());
    }

    @GetMapping("/provinces")
    public ResponseEntity<ApiResponse<List<ProvinceDto>>> getProvinces() {
        return ResponseEntity.ok(ApiResponse.<List<ProvinceDto>>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch all provinces data successfully!")
                .data(addressService.findAllProvinces())
                .build()
        );
    }

    @GetMapping("/provinces/{provinceCode}/districts")
    public ResponseEntity<ApiResponse<List<DistrictDto>>> getDistrictsByProvince(@PathVariable String provinceCode) {
        return ResponseEntity.ok(ApiResponse.<List<DistrictDto>>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch all districts data by province code successfully!")
                .data(addressService.findDistrictsByProvinceCode(provinceCode))
                .build()
        );
    }

    @GetMapping("/districts/{districtCode}/wards")
    public ResponseEntity<ApiResponse<List<WardDto>>> getWardsByDistrict(@PathVariable String districtCode) {
        return ResponseEntity.ok(ApiResponse.<List<WardDto>>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch all wards data by province code successfully!")
                .data(addressService.findWardsByDistrictCode(districtCode))
                .build()
        );
    }

    @GetMapping("/provinces/{id}")
    public ResponseEntity<ApiResponse<ProvinceDto>> getProvinceById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<ProvinceDto>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch province by id successfully!")
                .data(addressService.findProvinceDtoById(id))
                .build()
        );
    }

    @GetMapping("/districts/{id}")
    public ResponseEntity<ApiResponse<DistrictDto>> getDistrictById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<DistrictDto>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch district by id successfully!")
                .data(addressService.findDistrictDtoById(id))
                .build()
        );
    }

    @GetMapping("/wards/{id}")
    public ResponseEntity<ApiResponse<WardDto>> getWardById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<WardDto>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch ward by id successfully!")
                .data(addressService.findWardDtoById(id))
                .build()
        );
    }

    @GetMapping("/districts")
    public ResponseEntity<ApiResponse<List<DistrictDto>>> getDistricts() {
        return ResponseEntity.ok(ApiResponse.<List<DistrictDto>>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch all districts data successfully!")
                .data(addressService.findAllDistricts())
                .build()
        );
    }
    @GetMapping("/wards")
    public ResponseEntity<ApiResponse<List<WardDto>>> getWards() {
        return ResponseEntity.ok(ApiResponse.<List<WardDto>>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("fetch all wards data successfully!")
                .data(addressService.findAllWards())
                .build()
        );
    }
}
