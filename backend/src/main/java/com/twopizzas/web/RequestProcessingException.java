package com.twopizzas.web;

import com.fasterxml.jackson.core.JsonProcessingException;

public class RequestProcessingException extends RuntimeException {
    public RequestProcessingException(String message, Exception cause) {
        super(message, cause);
    }
}
