package com.twopizzas.port.data;

public class DataMappingException extends RuntimeException {
    public DataMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataMappingException(String message) {
        super(message);
    }
}
