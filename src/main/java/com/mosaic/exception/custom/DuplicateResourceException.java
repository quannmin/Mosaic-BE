package com.mosaic.exception.custom;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(
                String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue),
                "DUPLICATE_RESOURCE"
        );
    }

}
