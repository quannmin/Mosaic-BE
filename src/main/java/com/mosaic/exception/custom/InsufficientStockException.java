package com.mosaic.exception.custom;

public class InsufficientStockException extends BusinessException{
    public InsufficientStockException(String message){
        super(message, "INSUFFICIENT_STOCK");
    }
}
