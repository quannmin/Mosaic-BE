package com.mosaic.exception;

public class ElementNotFoundException extends RuntimeException{
    public ElementNotFoundException(String message) {
        super(message);
    }
}
