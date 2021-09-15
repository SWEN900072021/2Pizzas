package com.twopizzas.web;

import lombok.Getter;

@Getter
public class RestResponse<T> {

    private final HttpStatus status;
    private final T body;

    private RestResponse(HttpStatus status, T body) {
        this.status = status;
        this.body = body;
    }

    public static <T> RestResponse<T> ok(T body) {
        return new RestResponse<>(HttpStatus.OK, body);
    }

    public static RestResponse<Void> ok() {
        return new RestResponse<>(HttpStatus.OK, null);
    }
}
