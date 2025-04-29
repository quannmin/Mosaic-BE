package com.mosaic.domain.response.localtion;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceDto {
    private String name;
    private String code;
    private String division_type;
    private List<DistrictDto> districts;
}
