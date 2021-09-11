package com.twopizzas.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface HttpRequestDelegate {
    boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
    PathResolver getPathResolver();
    Set<HttpMethod> getMethods();
}

