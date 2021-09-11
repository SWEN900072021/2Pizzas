package com.twopizzas.di;

import com.twopizzas.di.testroot.InterfaceComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;

public class QualifiedBeanSpecificationTests {

    @Test
    @DisplayName("GIVEN bean has no qualifier WHEN filter THEN not returned")
    void test() {
        // GIVEN
        Bean<InterfaceComponent> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getQualifier()).thenReturn(null);
        ComponentSpecification<InterfaceComponent> wrapped = Mockito.mock(ComponentSpecification.class);
        Mockito.when(wrapped.filter(Mockito.any())).thenAnswer(args -> args.getArgument(0));

        // WHEN
        Collection<Bean<InterfaceComponent>> filtered = new QualifiedBeanSpecification<>(
                "qualifier", wrapped
        ).filter(Collections.singletonList(bean));

        // THEN
        Assertions.assertNotNull(filtered);
        Assertions.assertTrue(filtered.isEmpty());
    }

    @Test
    @DisplayName("GIVEN bean has different qualifier WHEN filter THEN not returned")
    void test2() {
        // GIVEN
        Bean<InterfaceComponent> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getQualifier()).thenReturn("differnt");
        ComponentSpecification<InterfaceComponent> wrapped = Mockito.mock(ComponentSpecification.class);
        Mockito.when(wrapped.filter(Mockito.any())).thenAnswer(args -> args.getArgument(0));

        // WHEN
        Collection<Bean<InterfaceComponent>> filtered = new QualifiedBeanSpecification<>(
                "qualifier", wrapped
        ).filter(Collections.singletonList(bean));

        // THEN
        Assertions.assertNotNull(filtered);
        Assertions.assertTrue(filtered.isEmpty());
    }

    @Test
    @DisplayName("GIVEN bean has different qualifier WHEN filter THEN not returned")
    void test3() {
        // GIVEN
        Bean<InterfaceComponent> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getQualifier()).thenReturn("qualifier");
        ComponentSpecification<InterfaceComponent> wrapped = Mockito.mock(ComponentSpecification.class);
        Mockito.when(wrapped.filter(Mockito.any())).thenAnswer(args -> args.getArgument(0));

        // WHEN
        Collection<Bean<InterfaceComponent>> filtered = new QualifiedBeanSpecification<>(
                "qualifier", wrapped
        ).filter(Collections.singletonList(bean));

        // THEN
        Assertions.assertNotNull(filtered);
        Assertions.assertEquals(1, filtered.size());
        Assertions.assertEquals(bean, filtered.iterator().next());
    }
}
