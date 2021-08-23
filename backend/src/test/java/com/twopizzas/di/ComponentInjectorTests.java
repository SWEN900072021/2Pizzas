package com.twopizzas.di;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class ComponentInjectorTests {

    private ComponentStore componentStore;

    @Mock
    private ComponentLoader componentLoader;

    private ComponentInjector componentInjector;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        componentStore = new ComponentStore();
        componentInjector = new ComponentInjector(componentLoader);
    }

    @Test
    @DisplayName("GIVEN component with no dependencies WHEN injectAll invoked THEN returns map with single component")
    void test() {
        // GIVEN
        Mockito.when(componentLoader.scanAllComponents()).thenReturn(Arrays.asList(
                ClientComponent.class,
                ClientComponentWithDependencies.class,
                ComponentDependency.class,
                ComponentDependencyOther.class
        ));

        ComponentDependency componentDependency = new ComponentDependency();
        ComponentDependencyOther componentDependencyOther = new ComponentDependencyOther();
        ClientComponent clientComponent = new ClientComponent();
        ClientComponentWithDependencies clientComponentWithDependencies = new ClientComponentWithDependencies();

        Mockito.doReturn(Collections.emptyList()).when(componentLoader)
                .getDependencies(Mockito.eq(ClientComponent.class));

        Mockito.doReturn(Collections.emptyList()).when(componentLoader)
                .getDependencies(Mockito.eq(ComponentDependency.class));

        Mockito.doReturn(Collections.emptyList()).when(componentLoader)
                .getDependencies(Mockito.eq(ComponentDependencyOther.class));

        Mockito.doReturn(Arrays.asList(
                        ComponentDependency.class,
                        ComponentDependencyOther.class
                )).when(componentLoader)
                .getDependencies(Mockito.eq(ClientComponentWithDependencies.class));

        Mockito.doReturn(componentDependency).when(componentLoader)
                .construct(Mockito.eq(ComponentDependency.class), Mockito.any());

        Mockito.doReturn(componentDependencyOther).when(componentLoader)
                .construct(Mockito.eq(ComponentDependencyOther.class), Mockito.any());

        Mockito.doReturn(clientComponent).when(componentLoader)
                .construct(Mockito.eq(ClientComponent.class), Mockito.any());

        Mockito.doReturn(clientComponentWithDependencies).when(componentLoader)
                .construct(Mockito.eq(ClientComponentWithDependencies.class), Mockito.any());

        // WHEN
        componentInjector.injectAll(componentStore);

        // THEN
        Assertions.assertEquals(4, componentStore.getAll().size());
        Assertions.assertTrue(componentStore.getAll().contains(componentDependency));
        Assertions.assertTrue(componentStore.getAll().contains(componentDependencyOther));
        Assertions.assertTrue(componentStore.getAll().contains(clientComponent));
        Assertions.assertTrue(componentStore.getAll().contains(clientComponentWithDependencies));
        Mockito.verify(componentLoader).scanAllComponents();
        Mockito.verify(componentLoader).getDependencies(Mockito.eq(ComponentDependency.class));
        Mockito.verify(componentLoader).getDependencies(Mockito.eq(ComponentDependencyOther.class));
        Mockito.verify(componentLoader).getDependencies(Mockito.eq(ClientComponent.class));
        Mockito.verify(componentLoader).getDependencies(Mockito.eq(ClientComponentWithDependencies.class));
        Mockito.verify(componentLoader).construct(Mockito.eq(ComponentDependency.class), Mockito.eq(Collections.emptyList()));
        Mockito.verify(componentLoader).construct(Mockito.eq(ComponentDependencyOther.class), Mockito.eq(Collections.emptyList()));
        Mockito.verify(componentLoader).construct(Mockito.eq(ClientComponent.class), Mockito.eq(Collections.emptyList()));
        Mockito.verify(componentLoader).construct(Mockito.eq(ClientComponentWithDependencies.class), Mockito.eq(Arrays.asList(
                componentDependency, componentDependencyOther
        )));
    }

    @Component
    private static class ClientComponent { }

    @Component
    private static class ClientComponentWithDependencies { }

    @Component
    private static class ComponentDependency { }

    @Component
    private static class ComponentDependencyOther { }
}
