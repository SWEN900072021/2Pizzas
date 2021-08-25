package com.twopizzas.di;

import com.twopizzas.di.testroot.TestDependency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

public class PrimaryBeanResolverTests {

    @Test
    @DisplayName("GIVEN no primary beans WHEN resolve THEN all beans returned")
    void test() {
        // GIVEN
        Bean<TestDependency> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.isPrimary()).thenReturn(false);
        Bean<TestDependency> bean2 = Mockito.mock(Bean.class);
        Mockito.when(bean2.isPrimary()).thenReturn(false);
        BaseBeanResolver baseBeanResolver = Mockito.mock(BaseBeanResolver.class);
        Mockito.when(baseBeanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(args -> args.getArgument(1));
        ComponentSpecification<TestDependency> specification = Mockito.mock(ComponentSpecification.class);

        // WHEN
        Collection<Bean<TestDependency>> resolved = new PrimaryBeanResolver(baseBeanResolver)
                .resolve(specification, Arrays.asList(bean, bean2));

        // THEN
        Assertions.assertNotNull(resolved);
        Assertions.assertEquals(2, resolved.size());
        Assertions.assertTrue(resolved.contains(bean));
        Assertions.assertTrue(resolved.contains(bean2));
    }

    @Test
    @DisplayName("GIVEN primary bean WHEN resolve THEN that bean returned")
    void test2() {
        // GIVEN
        Bean<TestDependency> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.isPrimary()).thenReturn(true);
        Bean<TestDependency> bean2 = Mockito.mock(Bean.class);
        Mockito.when(bean2.isPrimary()).thenReturn(false);
        BaseBeanResolver baseBeanResolver = Mockito.mock(BaseBeanResolver.class);
        Mockito.when(baseBeanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(args -> args.getArgument(1));
        ComponentSpecification<TestDependency> specification = Mockito.mock(ComponentSpecification.class);

        // WHEN
        Collection<Bean<TestDependency>> resolved = new PrimaryBeanResolver(baseBeanResolver)
                .resolve(specification, Arrays.asList(bean, bean2));

        // THEN
        Assertions.assertNotNull(resolved);
        Assertions.assertEquals(1, resolved.size());
        Assertions.assertTrue(resolved.contains(bean));
        Assertions.assertFalse(resolved.contains(bean2));
    }
}
