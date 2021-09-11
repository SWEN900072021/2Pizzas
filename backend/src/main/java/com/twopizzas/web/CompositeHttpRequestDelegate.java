package com.twopizzas.web;

import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class CompositeHttpRequestDelegate implements HttpRequestDelegate {

    private PathResolver pathResolver;
    private List<HttpRequestDelegate> delegates;

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

    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (pathResolver.test(request.getRequestURI()).isMatch()) {
            for (HttpRequestDelegate delegate : delegates) {
                if (delegate.handle(request, response)) {
                    return true;
                }
            }
            throw new HttpException(HttpStatus.METHOD_NOT_SUPPORTED);
        }
        return false;
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
