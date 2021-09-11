package com.twopizzas.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BaseRequestDelegate implements HttpRequestDelegate {
    private final PathResolver pathResolver;
    private final HttpMethod method;
    private final Object targetController;
    private final Method handler;
    private final ObjectMapper mapper;

    public BaseRequestDelegate(PathResolver pathResolver, HttpMethod method, Object targetController, Method handler, ObjectMapper mapper) {
        this.pathResolver = pathResolver;
        this.method = method;
        this.targetController = targetController;
        this.handler = handler;
        this.mapper = mapper;
    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PathResolver.PathResult result = pathResolver.test(request.getPathInfo());
        if (result.isMatch() && request.getMethod().equals(method.name())) {
            List<Object> args = getArgsFromRequest(request, result);
            RestResponse<?> restResponse = invokeHandler(args);
            sendResponse(response, restResponse);
            return true;
        }
        return false;
    }

    @Override
    public PathResolver getPathResolver() {
        return pathResolver;
    }

    private List<Object> getArgsFromRequest(HttpServletRequest request, PathResolver.PathResult pathResult) {
        String body = readBody(request);
        return Arrays.stream(handler.getParameters()).map(
                p -> {
                    if (p.getAnnotation(PathVariable.class) != null) {
                        return pathResult.getPathVariable(p.getName()).orElse(null);
                    }

                    if (p.getAnnotation(RequestBody.class) != null) {
                        try {
                            return mapper.readValue(body, p.getType());
                        } catch (JsonProcessingException e) {
                            throw new RequestProcessingException(String.format("failed to read request body [%s] to class [%s]: %s", body, p.getType(), e.getMessage()), e);
                        }
                    }
                    return null;
                }
        ).collect(Collectors.toList());
    }

    private String readBody(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RequestProcessingException(String.format("failed to read request body as string from servlet reader: %s", e.getMessage()), e);
        }
    }

    private RestResponse<?> invokeHandler(List<Object> args) {
        try {
            return (RestResponse<?>) handler.invoke(targetController, args);
        } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
            throw new RequestProcessingException(String.format("failed to read request body as string from servlet reader: %s", e.getMessage()), e);
        }
    }

    private void sendResponse(HttpServletResponse response, RestResponse<?> restResponse) {
        try {
            response.getWriter().print(mapper.writeValueAsString(restResponse.getBody()));
            response.setContentType("application/json");
            response.setStatus(restResponse.getStatus());
        } catch (IOException e) {
            throw new RequestProcessingException(String.format("failed to send response via %s: %s", response.getClass(), e.getMessage()), e);
        }
    }
}
