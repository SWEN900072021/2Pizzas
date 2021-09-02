package com.twopizzas.di.testroot;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.di.PostConstruct;
import com.twopizzas.di.Qualifier;

@Component("two")
public class CycleDependencyTwo implements CycleDependency {
    CycleDependency cycleDependency;
    private boolean postConstruct;

    @Autowired
    CycleDependencyTwo(@Qualifier("one") CycleDependency  cycleDependency) {
        this.cycleDependency = cycleDependency;
    }

    @Override
    public CycleDependency getCycleDependency() {
        return cycleDependency;
    }

    public boolean getPostConstruct() {
        return postConstruct;
    }

    @PostConstruct
    public void init() {
        postConstruct = true;
    }
}
