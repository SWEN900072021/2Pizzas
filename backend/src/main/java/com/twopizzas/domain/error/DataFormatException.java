package com.twopizzas.domain.error;

public class DataFormatException extends RuntimeException {
    public DataFormatException(String message) {
        super(message);
    }
}