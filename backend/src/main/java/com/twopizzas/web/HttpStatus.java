package com.twopizzas.web;

import lombok.Getter;

@Getter
public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    UNAUTHORIZED(401, "Unauthorized"),
    METHOD_NOT_SUPPORTED(405, "Method Not Supported"),
    BAD_REQUEST(400, "Bad Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NO_CONTENT(204, "No Content"),
    FORBIDDEN(403, "Forbidden");

    private final int statusCode;
    private final String status;

    HttpStatus(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }
}
