package com.twopizzas.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twopizzas.auth.AuthenticationProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseRequestDelegate implements HttpRequestDelegate {
    private final PathResolver pathResolver;
    private final HttpMethod method;
    private final Object targetController;
    private final Method handler;
    private final ObjectMapper mapper;
    private final AuthenticationProvider authenticationProvider;


    public BaseRequestDelegate(PathResolver pathResolver, HttpMethod method, Object targetController, Method handler, ObjectMapper mapper, AuthenticationProvider authenticationProvider) {
        this.pathResolver = pathResolver;
        this.method = method;
        this.targetController = targetController;
        this.handler = handler;
        this.mapper = mapper;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public RestResponse<?> handle(HttpRequest request) throws Throwable {
        PathResolver.PathResult result = pathResolver.test(request.getPath());
        if (result.isMatch() && request.getMethod().equals(method)) {
            List<Object> args = getArgsFromRequest(request, result);
            return invokeHandler(args);
        }
        throw new RequestProcessingException(String.format(
                "handle invoked on delegate for request with path [%s] and method [%s] " +
                        "but delegate is only configured to handle requests with path [%s] and methods [%s]",
                request.getPath(), request.getMethod(), pathResolver.getPath(), method
                ));
    }

    @Override
    public PathResolver getPathResolver() {
        return pathResolver;
    }

    @Override
    public Set<HttpMethod> getMethods() {
        return Collections.singleton(method);
    }

    private List<Object> getArgsFromRequest(HttpRequest request, PathResolver.PathResult pathResult) {
        return Arrays.stream(handler.getParameters()).map(
                p -> {
                    PathVariable pathVariableAnnotation = p.getAnnotation(PathVariable.class);
                    if (pathVariableAnnotation != null) {
                        return pathResult.getPathVariable(pathVariableAnnotation.value()).orElse(null);
                    }

                    QueryParameter queryParameterAnnotation = p.getAnnotation(QueryParameter.class);
                    if (queryParameterAnnotation != null) {
                        return request.getQueries().get(queryParameterAnnotation.value());
                    }

                    if (p.getAnnotation(RequestBody.class) != null) {
                        try {
                            return mapper.readValue(request.getBody(), p.getType());
                        } catch (JsonProcessingException e) {
                            throw new RequestProcessingException(String.format("failed to read request body [%s] to class [%s]: %s", request.getBody(), p.getType(), e.getMessage()), e);
                        }
                    }
                    return null;
                }
        ).collect(Collectors.toList());
    }

    private RestResponse<?> invokeHandler(List<Object> args) throws Throwable {
        try {
            Object response = handler.invoke(targetController, args.toArray());

            if (response == null) {
                throw new RequestProcessingException(String.format("http handler [%s] with target [%s] returned null, handlers must return a valid response", handler.getName(), targetController.getClass()));
            }

            if (!(response instanceof RestResponse)) {
                throw new RequestProcessingException(String.format("http handler [%s] with target [%s] returned invalid response, handlers return type must be [%s]", handler.getName(), targetController.getClass(), RestResponse.class.getName()));
            }

            return (RestResponse<?>) response;

        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
