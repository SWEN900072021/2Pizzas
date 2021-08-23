package com.twopizzas.di;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ComponentStoreTests {

    private TestComponentStore componentStore;

    @Test
    @DisplayName("GIVEN component type is registered WHEN getComponent invoked THEN returns component type")
    void test() {
        // GIVEN
        StubComponent component = new StubComponent();
        Set<Object> store = Collections.singleton(component);
        componentStore = new TestComponentStore(store);

        // WHEN
        StubComponent retrieved = componentStore.get(StubComponent.class);

        // THEN
        Assertions.assertEquals(component, retrieved);
    }

    @Test
    @DisplayName("GIVEN component sub type is registered WHEN getComponent invoked THEN returns component sub type")
    void test2() {
        // GIVEN
        SubStubComponent component = new SubStubComponent();
        Set<Object> store = Collections.singleton(component);
        componentStore = new TestComponentStore(store);

        // WHEN
        StubComponent retrieved = componentStore.get(StubComponent.class);

        // THEN
        Assertions.assertEquals(component, retrieved);
    }

    @Test
    @DisplayName("GIVEN component sub type not registered WHEN getComponent invoked THEN returns null")
    void test3() {
        // GIVEN
        StubComponent component = new StubComponent();
        Set<Object> store = Collections.singleton(component);
        componentStore = new TestComponentStore(store);

        // WHEN
        Object retrieved = componentStore.get(SubStubComponent.class);

        // THEN
        Assertions.assertNull(retrieved);
    }

    @Test
    @DisplayName("GIVEN multiple components registered WHEN getComponent invoked THEN throws")
    void test4() {
        // GIVEN
        StubComponent component = new StubComponent();
        SubStubComponent subComponent = new SubStubComponent();
        Set<Object> store = new HashSet<>(Arrays.asList(component, subComponent));
        componentStore = new TestComponentStore(store);

        // WHEN + THEN
        Assertions.assertThrows(DuplicateComponentException.class, () -> componentStore.get(StubComponent.class));
    }

    @Test
    @DisplayName("GIVEN component sub type not registered WHEN getComponent invoked THEN returns null")
    void test5() {
        // GIVEN
        StubComponent component = new StubComponent();
        Set<Object> store = Collections.singleton(component);
        componentStore = new TestComponentStore(store);

        // WHEN
        Object retrieved = componentStore.get(SubStubComponent.class);

        // THEN
        Assertions.assertNull(retrieved);
    }

    @Test
    @DisplayName("GIVEN component type not registered WHEN registerComponent invoked THEN added to map")
    void test6() {
        // GIVEN
        StubComponent component = new StubComponent();
        Set<Object> store = new HashSet<>();
        componentStore = new TestComponentStore(store);

        // WHEN
        componentStore.register(component);

        // THEN
        Assertions.assertTrue(store.contains(component));
    }

    @Test
    @DisplayName("GIVEN component type is registered WHEN registerComponent invoked THEN throws")
    void test7() {
        // GIVEN
        SubStubComponent component = new SubStubComponent();
        Set<Object> store = new HashSet<>();
        store.add(component);
        componentStore = new TestComponentStore(store);

        // WHEN + THEN
        Assertions.assertThrows(DuplicateComponentException.class, () -> componentStore.register(new StubComponent()));
    }

    private static class StubComponent {}

    private static class SubStubComponent extends StubComponent {}

    private static class TestComponentStore extends ComponentStore {
        TestComponentStore(Set<Object> store) {
            this.store = store;
        }
    }
}
