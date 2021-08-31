package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MapperRegistryImplTests {

    private DataMapperRegistry mapperRegistry;

    @BeforeEach
    void setup() {
        mapperRegistry = new DataMapperRegistryImpl();
    }

    @Test
    @DisplayName("GIVEN mapper registered WHEN getForClass invoked THEN returns that mapper")
    void test() {
        // GIVEN
        StubMapper mapper = Mockito.mock(StubMapper.class);
        Mockito.when(mapper.getEntityClass()).thenReturn(StubEntity.class);
        mapperRegistry.register(StubEntity.class, mapper);

        // WHEN
        DataMapper<?, ?, ?> retrieved = mapperRegistry.getForClass(StubEntity.class);

        // THE
        Assertions.assertTrue(retrieved instanceof StubMapper);
        Assertions.assertEquals(mapper, retrieved);
    }

    @Test
    @DisplayName("GIVEN mapper not registered WHEN getForClass invoked THEN throws")
    void test2() {
        // GIVEN
        // register nothing

        // WHEN + THEN
        Assertions.assertThrows(DataMapperRegistryImpl.MapperNotFound.class, () -> mapperRegistry.getForClass(StubEntity.class));
    }

    @Test
    @DisplayName("GIVEN mapper registered for super class WHEN getForClass invoked for sub class THEN throws")
    void test3() {
        // GIVEN
        StubMapper mapper = Mockito.mock(StubMapper.class);
        Mockito.when(mapper.getEntityClass()).thenReturn(StubEntity.class);
        mapperRegistry.register(StubEntity.class, mapper);

        // WHEN + THEN
        Assertions.assertThrows(DataMapperRegistryImpl.MapperNotFound.class, () -> mapperRegistry.getForClass(StubSubEntity.class));
    }

    @Test
    @DisplayName("GIVEN mapper registered for sub class WHEN getForClass invoked for super class THEN throws")
    void test4() {
        // GIVEN
        StubSubMapper mapper = Mockito.mock(StubSubMapper.class);
        Mockito.when(mapper.getEntityClass()).thenReturn(StubSubEntity.class);
        mapperRegistry.register(StubSubEntity.class, mapper);

        // WHEN + THEN
        Assertions.assertThrows(DataMapperRegistryImpl.MapperNotFound.class, () -> mapperRegistry.getForClass(StubEntity .class));
    }

    @Test
    @DisplayName("GIVEN mapper already registered WHEN register mapper for same class THEN throws")
    void test5() {
        // GIVEN
        StubSubMapper mapper = Mockito.mock(StubSubMapper.class);
        Mockito.when(mapper.getEntityClass()).thenReturn(StubSubEntity.class);
        mapperRegistry.register(StubSubEntity.class, mapper);

        // WHEN + THEN
        Assertions.assertThrows(DataMapperRegistryImpl.MapperRegistrationException.class, () -> mapperRegistry.register(StubSubEntity.class, mapper));
    }
}
