package com.mosaic.exception.custom;

public class MessagingException extends BusinessException{
    public MessagingException(String resourceName, String fieldName, Object fieldValue) {
        super(
                String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue),
                "DUPLICATE_RESOURCE"
        );
    }

    public MessagingException(String message) {
        super(
                message,
                "DUPLICATE_RESOURCE"
        );
    }
}
