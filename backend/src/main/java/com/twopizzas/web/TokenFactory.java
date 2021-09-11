package com.twopizzas.web;

public class TokenFactory {
    PathToken make(String token) {
        if (token.length() >= 2 && token.charAt(0) == '{' && token.charAt(token.length() - 1) == '}') {
            return new PathVariableToken(token.substring(1, token.length() - 1));
        }
        return new PathToken(token);
    }
}
