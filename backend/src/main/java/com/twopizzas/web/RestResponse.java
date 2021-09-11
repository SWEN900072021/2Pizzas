package com.twopizzas.web;

import lombok.Getter;

@Getter
public class RestResponse<T> {

    private final int status;
    private final T body;

    private RestResponse(int status, T body) {
        this.status = status;
        this.body = body;
    }

    public static <T> RestResponse<T> ok(T body) {
        return new RestResponse<>(200, body);
    }

    public static RestResponse<Void> ok() {
        return new RestResponse<>(200, null);
    }
}
