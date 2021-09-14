package com.twopizzas.web;

import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class CompositeHttpRequestDelegate implements HttpRequestDelegate {

    private PathResolver pathResolver;
    private final List<HttpRequestDelegate> delegates = new ArrayList<>();

    boolean handles(PathResolver resolver) {
        if (pathResolver == null) {
            return true;
        }
        return resolversAreEquivalent(pathResolver, resolver);
    }

    void registerDelegate(HttpRequestDelegate delegate) {
        if (pathResolver == null) {
            pathResolver = delegate.getPathResolver();
            delegates.add(delegate);
        } else {
            if (!resolversAreEquivalent(pathResolver, delegate.getPathResolver())) {
                throw new RuntimeException("attempt to register delegate with non equivalent path resolver");
            }

            if (!Sets.intersection(delegate.getMethods(), getMethods()).isEmpty()) {
                throw new RuntimeException("duplicate handlers detected for path");
            }

            delegates.add(delegate);
        }
    }

    private boolean resolversAreEquivalent(PathResolver resolver, PathResolver other) {
        PathResolver.PathResult result = resolver.test(other.getPath());
        if (!result.isMatch()) {
            result = other.test(resolver.getPath());
        }
        return result.isMatch();
     }

    @Override
    public RestResponse<?> handle(HttpRequest request) throws Throwable {
        if (pathResolver.test(request.getPath()).isMatch()) {
            for (HttpRequestDelegate delegate : delegates) {
                if (delegate.getMethods().contains(request.getMethod())) {
                    RestResponse<?> response = delegate.handle(request);
                    if (response != null) {
                        return response;
                    }
                }
            }
            throw new HttpException(HttpStatus.METHOD_NOT_SUPPORTED);
        }
        return null;
    }

    @Override
    public PathResolver getPathResolver() {
        return pathResolver;
    }

    @Override
    public Set<HttpMethod> getMethods() {
        return delegates.stream().flatMap(d -> d.getMethods().stream()).collect(Collectors.toSet());
    }
}
