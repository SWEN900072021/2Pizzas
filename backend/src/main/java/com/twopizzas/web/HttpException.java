package com.twopizzas.web;

import lombok.Getter;

@Getter
public class HttpException extends Exception {
    private final HttpStatus status;
    private final String reason;

    public HttpException(HttpStatus status, String reason, Throwable cause) {
        super(String.format("http error [%s] and code [%s] with reason [%s]: %s", status.getStatus(), status.getStatusCode(), reason, cause.getMessage()));
        this.status = status;
        this.reason = reason;
    }

    public HttpException(HttpStatus status) {
        super(String.format("http error [%s] and code [%s]", status.getStatus(), status.getStatusCode()));
        this.status = status;
        this.reason = status.getStatus();
    }
}
