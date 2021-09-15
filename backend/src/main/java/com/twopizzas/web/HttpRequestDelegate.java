package com.twopizzas.web;

import java.util.Set;

public interface HttpRequestDelegate {
    RestResponse<?> handle(HttpRequest request) throws Throwable;
    PathResolver getPathResolver();
    Set<HttpMethod> getMethods();
}

