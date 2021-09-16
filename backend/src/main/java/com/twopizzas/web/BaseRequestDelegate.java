package com.twopizzas.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twopizzas.auth.AuthenticationProvider;
import com.twopizzas.domain.user.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

            Optional<User> authenticatedUser = Optional.empty();
            Authenticated authenticatedAnnotation = handler.getDeclaredAnnotation(Authenticated.class);
            if (handler.getDeclaredAnnotation(Authenticated.class) != null) {
                boolean authorized = false;
                authenticatedUser = doAuthentication(request);
                if (authenticatedUser.isPresent() && authenticatedAnnotation.value().length > 0) {
                    if (Arrays.asList(authenticatedAnnotation.value()).contains(authenticatedUser.get().getUserType())) {
                        authorized = true;
                    }
                }

                if (!authorized) {
                    throw new HttpException(HttpStatus.UNAUTHORIZED);
                }
            }

            List<Object> args = getArgsFromRequest(request, result, authenticatedUser.orElse(null));
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

    private List<Object> getArgsFromRequest(HttpRequest request, PathResolver.PathResult pathResult, User user) {
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

                    if (p.getType().equals(User.class)) {
                        return user;
                    }

                    return null;
                }
        ).collect(Collectors.toList());
    }

    private Optional<User> doAuthentication(HttpRequest request) throws HttpException {
        if (request.getHeaders().containsKey("authorization")) {
            String auth = request.getHeaders().get("authorization");
            String[] bearerAndToken = auth.split(" ");
            if (bearerAndToken.length != 2) {
                throw new HttpException(HttpStatus.BAD_REQUEST, "invalid authorization token");
            }
            if (!bearerAndToken[0].equals("Bearer")) {
                throw new HttpException(HttpStatus.BAD_REQUEST, "authorization must be of type Bearer");
            }
            return authenticationProvider.authenticate(bearerAndToken[1]);
        }
        return Optional.empty();
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
