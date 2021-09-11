package com.twopizzas.di.testroot;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.di.Primary;
import com.twopizzas.di.Profile;

@Component
@Profile("test")
@Primary
public class TestDependencyOther implements TestDependencyOtherInterface {

    @Autowired
    public TestDependencyOther() { }
}
