package com.twopizzas.di;

public class AmbiguousDependency extends ApplicationContextException {
    public AmbiguousDependency(String s) {
        super(s);
    }
}
