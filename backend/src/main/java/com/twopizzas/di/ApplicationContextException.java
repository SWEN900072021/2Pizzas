package com.twopizzas.di;

public class ApplicationContextException extends RuntimeException {
    ApplicationContextException(String message) {
        super(message);
    }

    ApplicationContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
