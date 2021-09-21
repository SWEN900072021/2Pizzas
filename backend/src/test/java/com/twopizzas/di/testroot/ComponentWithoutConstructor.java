package com.twopizzas.di.testroot;

public class ComponentWithoutConstructor {
    private final TestDependency dependency;

    ComponentWithoutConstructor(TestDependency dependency) {
        this.dependency = dependency;
    }
}
