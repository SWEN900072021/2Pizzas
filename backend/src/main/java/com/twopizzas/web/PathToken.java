package com.twopizzas.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class PathToken {
    protected String value;

    public boolean matches(String segment) {
        return this.value.equals(segment);
    }

    public Optional<Variable> getVariable(String segment) {
        return Optional.empty();
    }
}
