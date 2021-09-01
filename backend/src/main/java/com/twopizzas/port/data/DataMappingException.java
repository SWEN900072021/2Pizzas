package com.twopizzas.port.data;

import com.twopizzas.data.DataMapper;

public class DataMappingException extends RuntimeException {
    DataMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
