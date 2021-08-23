package com.twopizzas.di.testroot;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.di.PostConstruct;

@Component
public class TestDependency {

    private boolean initialized;

    @Autowired
    public TestDependency() { }

    @PostConstruct
    void init() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
