package com.twopizzas.web;

public class RequestProcessingException extends RuntimeException {
    public RequestProcessingException(String message, Exception cause) {
        super(message, cause);
    }

    public RequestProcessingException(String message) {
        super(message);
    }
}
