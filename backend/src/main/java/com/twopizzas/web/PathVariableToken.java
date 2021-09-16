package com.twopizzas.web;

import java.util.Optional;

public class PathVariableToken extends PathToken {

    public PathVariableToken(String value) {
        super(value);
    }

    @Override
    public boolean matches(String segment) {
        return true;
    }

    @Override
    public Optional<Variable> getVariable(String segment) {
        return Optional.of(new Variable(value, segment));
    }
}
