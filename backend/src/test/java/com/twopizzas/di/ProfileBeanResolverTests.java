package com.twopizzas.di;

import com.twopizzas.di.testroot.TestDependency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ProfileBeanResolverTests {

    @Test
    @DisplayName("GIVEN no beans match profile WHEN resolve THEN all beans returned")
    void test() {
        // GIVEN
        Bean<TestDependency> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getProfiles()).thenReturn(Collections.emptyList());
        Bean<TestDependency> bean2 = Mockito.mock(Bean.class);
        Mockito.when(bean2.getProfiles()).thenReturn(Arrays.asList("test", "model"));
        BaseBeanResolver baseBeanResolver = Mockito.mock(BaseBeanResolver.class);
        Mockito.when(baseBeanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(args -> args.getArgument(1));
        TypedComponentSpecification<TestDependency> specification = Mockito.mock(TypedComponentSpecification.class);

        // WHEN
        Collection<Bean<TestDependency>> resolved = new ProfileBeanResolver(baseBeanResolver, "prod")
                .resolve(specification, Arrays.asList(bean, bean2));

        // THEN
        Assertions.assertNotNull(resolved);
        Assertions.assertEquals(2, resolved.size());
        Assertions.assertTrue(resolved.contains(bean));
        Assertions.assertTrue(resolved.contains(bean2));
    }

    @Test
    @DisplayName("GIVEN one bean matches profile WHEN resolve THEN that bean returned")
    void test2() {
        // GIVEN
        Bean<TestDependency> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getProfiles()).thenReturn(Collections.singletonList("prod"));
        Bean<TestDependency> bean2 = Mockito.mock(Bean.class);
        Mockito.when(bean2.getProfiles()).thenReturn(Arrays.asList("test", "model"));
        BaseBeanResolver baseBeanResolver = Mockito.mock(BaseBeanResolver.class);
        Mockito.when(baseBeanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(args -> args.getArgument(1));
        TypedComponentSpecification<TestDependency> specification = Mockito.mock(TypedComponentSpecification.class);

        // WHEN
        Collection<Bean<TestDependency>> resolved = new ProfileBeanResolver(baseBeanResolver, "prod")
                .resolve(specification, Arrays.asList(bean, bean2));

        // THEN
        Assertions.assertNotNull(resolved);
        Assertions.assertEquals(1, resolved.size());
        Assertions.assertTrue(resolved.contains(bean));
        Assertions.assertFalse(resolved.contains(bean2));
    }

    @Test
    @DisplayName("GIVEN multiple bean match profile WHEN resolve THEN all beans returned")
    void test3() {
        // GIVEN
        Bean<TestDependency> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getProfiles()).thenReturn(Collections.singletonList("prod"));
        Bean<TestDependency> bean2 = Mockito.mock(Bean.class);
        Mockito.when(bean2.getProfiles()).thenReturn(Arrays.asList("test", "prod"));
        BaseBeanResolver baseBeanResolver = Mockito.mock(BaseBeanResolver.class);
        Mockito.when(baseBeanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(args -> args.getArgument(1));
        TypedComponentSpecification<TestDependency> specification = Mockito.mock(TypedComponentSpecification.class);

        // WHEN
        Collection<Bean<TestDependency>> resolved = new ProfileBeanResolver(baseBeanResolver, "prod")
                .resolve(specification, Arrays.asList(bean, bean2));

        // THEN
        Assertions.assertNotNull(resolved);
        Assertions.assertEquals(2, resolved.size());
        Assertions.assertTrue(resolved.contains(bean));
        Assertions.assertTrue(resolved.contains(bean2));
    }
}
