package com.twopizzas.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PathResolver {

    @Getter
    private String path;
    private List<PathToken> tokens;

    static PathResolver of(String path) {
        TokenFactory tokenFactory = new TokenFactory();
        List<PathToken> tokens = Arrays.stream(path.split("/")).map(
                tokenFactory::make
        ).collect(Collectors.toList());
        return new PathResolver(path, tokens);
    }

    public PathResult test(String path) {
        String[] segments = path.split("/");
        Map<String, String> variables = new HashMap<>();
        if (segments.length == tokens.size()) {
            for (int i = 0; i < segments.length; i++) {
                PathToken token = tokens.get(i);
                if (token.matches(segments[i])) {
                    token.getVariable(segments[i])
                            .ifPresent(s -> variables.put(s.getName(), s.getValue()));
                } else {
                    return new PathResult(false);
                }
            }
            return new PathResult(true, variables);
        }
        return new PathResult(false);
    }

    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PathResult {

        @Getter
        private final boolean match;
        private Map<String, String> variables;

        Optional<String> getPathVariable(String name) {
            if (variables == null) {
                return Optional.empty();
            }
            return Optional.of(variables.get(name));
        }
    }
}
