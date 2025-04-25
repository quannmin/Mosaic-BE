package com.mosaic.domain.localtion;

import lombok.Data;

import java.util.List;

@Data
public class DistrictDto {
    private String name;
    private String code;
    private String division_type;
    private List<WardDto> wards;
}
