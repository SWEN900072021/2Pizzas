package com.twopizzas.di.testroot;

public class ComponentWithoutConstructor {
    private TestDependency dependency;

    ComponentWithoutConstructor(TestDependency dependency) {
        this.dependency = dependency;
    }
}
