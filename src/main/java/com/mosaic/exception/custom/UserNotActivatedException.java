package com.mosaic.exception.custom;

public class UserNotActivatedException extends BusinessException {

    public UserNotActivatedException(String username) {
        super(
                String.format("User %s is not activated", username),
                "USER_NOT_ACTIVATED"
        );
    }

    public UserNotActivatedException(String message, String username) {
        super(
                message,
                "USER_NOT_ACTIVATED"
        );
    }
}
