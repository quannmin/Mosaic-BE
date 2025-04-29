package com.mosaic.exception.custom;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException{
    private final String errorCode;

    protected BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
