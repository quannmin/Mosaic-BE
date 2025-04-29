package com.mosaic.exception.custom;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message, "BAD_REQUEST");
    }
}
