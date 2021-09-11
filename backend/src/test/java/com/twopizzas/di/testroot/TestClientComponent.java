package com.twopizzas.di.testroot;

import com.twopizzas.di.*;

@Component
public class TestClientComponent {

    private final TestDependencyInterface testDependency;
    private final TestDependencyOtherInterface testDependencyOther;
    private final InterfaceComponent interfaceComponent;

    @Autowired
    public TestClientComponent(@Qualifier("qualifier") TestDependencyInterface testDependency, TestDependencyOtherInterface testDependencyOther, InterfaceComponent interfaceComponent) {
        this.testDependency = testDependency;
        this.testDependencyOther = testDependencyOther;
        this.interfaceComponent = interfaceComponent;
    }

    public TestDependencyInterface getTestDependency() {
        return testDependency;
    }

    public TestDependencyOtherInterface getTestDependencyOther() {
        return testDependencyOther;
    }

    public InterfaceComponent getInterfaceComponent() { return interfaceComponent; }
}
