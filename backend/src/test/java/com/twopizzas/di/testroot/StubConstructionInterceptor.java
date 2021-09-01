package com.twopizzas.di.testroot;

import com.twopizzas.di.ComponentConstructionInterceptor;
import com.twopizzas.di.ComponentManager;

public class StubConstructionInterceptor implements ComponentConstructionInterceptor {
    @Override
    public <T> T intercept(T component, ComponentManager componentManager) {
        return null;
    }
}
