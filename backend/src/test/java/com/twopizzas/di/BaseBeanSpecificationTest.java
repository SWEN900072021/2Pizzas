package com.twopizzas.di;

import com.twopizzas.di.testroot.InterfaceComponent;
import com.twopizzas.di.testroot.InterfaceComponentImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;

public class BaseBeanSpecificationTest {

    @Test
    @DisplayName("GIVEN bean class is same as specification WHEN filter THEN returned")
    void test() {
        // GIVEN
        Bean<InterfaceComponentImpl> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getClasz()).thenReturn(InterfaceComponentImpl.class);

        // WHEN
        Collection<Bean<InterfaceComponentImpl>> filtered = new BaseBeanSpecification<InterfaceComponentImpl>(
                InterfaceComponentImpl.class
        ).filter(Collections.singletonList(bean));

        // THEN
        Assertions.assertNotNull(filtered);
        Assertions.assertEquals(1, filtered.size());
        Assertions.assertEquals(bean, filtered.iterator().next());
    }

    @Test
    @DisplayName("GIVEN bean class is sub type of specification WHEN filter THEN returned")
    void test2() {
        // GIVEN
        Bean<InterfaceComponentImpl> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getClasz()).thenReturn(InterfaceComponentImpl.class);

        // WHEN
        Collection<Bean<InterfaceComponent>> filtered = new BaseBeanSpecification<InterfaceComponent>(
                InterfaceComponent.class
        ).filter(Collections.singletonList(bean));

        // THEN
        Assertions.assertNotNull(filtered);
        Assertions.assertEquals(1, filtered.size());
        Assertions.assertEquals(bean, filtered.iterator().next());
    }

    @Test
    @DisplayName("GIVEN bean class is not sub type of specification WHEN filter THEN not returned")
    void test3() {
        // GIVEN
        Bean<InterfaceComponent> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getClasz()).thenReturn(InterfaceComponent.class);

        // WHEN
        Collection<Bean<InterfaceComponentImpl>> filtered = new BaseBeanSpecification<InterfaceComponentImpl>(
                InterfaceComponentImpl.class
        ).filter(Collections.singletonList(bean));

        // THEN
        Assertions.assertNotNull(filtered);
        Assertions.assertTrue(filtered.isEmpty());
    }
}
