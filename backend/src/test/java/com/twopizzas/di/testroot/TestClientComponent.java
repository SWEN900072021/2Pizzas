package com.twopizzas.di.testroot;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;

@Component
public class TestClientComponent {

    private final TestDependency testDependency;
    private final TestDependencyOther testDependencyOther;
    private final InterfaceComponent interfaceComponent;

    @Autowired
    TestClientComponent(TestDependency testDependency, TestDependencyOther testDependencyOther, InterfaceComponent interfaceComponent) {
        this.testDependency = testDependency;
        this.testDependencyOther = testDependencyOther;
        this.interfaceComponent = interfaceComponent;
    }

    public TestDependency getTestDependency() {
        return testDependency;
    }

    public TestDependencyOther getTestDependencyOther() {
        return testDependencyOther;
    }

    public InterfaceComponent getInterfaceComponent() { return interfaceComponent; }
}
