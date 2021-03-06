package com.twopizzas.di;

import com.twopizzas.di.testroot.CycleDependency;
import com.twopizzas.di.testroot.CycleDependencyOne;
import com.twopizzas.di.testroot.CycleDependencyTwo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class ApplicationContextIntegrationTests {

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("GIVEN valid project structure WHEN init THEN application context spun up")
    void test() {
        ApplicationContext applicationContext = new ApplicationContextImpl()
                .root("com.twopizzas.di.testroot")
                .init();

        CycleDependency one = applicationContext.getComponent(CycleDependency.class, "one");
        CycleDependency two = applicationContext.getComponent(CycleDependency.class, "two");

        CycleDependency oneCycleDependency = one.getCycleDependency().getCycleDependency();
        CycleDependency twoCycleDependency = two.getCycleDependency().getCycleDependency();

        Assertions.assertSame(((LazyComponentProxy) Proxy.getInvocationHandler(oneCycleDependency)).getWrapped(), one);
        Assertions.assertSame(((LazyComponentProxy) Proxy.getInvocationHandler(twoCycleDependency)).getWrapped(), two);

        Assertions.assertTrue(((CycleDependencyOne) one).getPostConstruct());
        Assertions.assertTrue(((CycleDependencyTwo) two).getPostConstruct());
    }
}
