package com.twopizzas.configuration;

public class ConfigurationContextException extends RuntimeException {
    ConfigurationContextException(String message) {
        super(message);
    }

    ConfigurationContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
