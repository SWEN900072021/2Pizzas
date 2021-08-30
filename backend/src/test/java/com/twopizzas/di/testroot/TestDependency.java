package com.twopizzas.di.testroot;

import com.twopizzas.di.*;

@Component("qualifier")
@Scope(ComponentScope.PROTOTYPE)
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
