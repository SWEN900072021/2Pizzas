package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MapperRegistryImplTests {

    private MapperRegistry mapperRegistry;

    @BeforeEach
    void setup() {
        mapperRegistry = new MapperRegistryImpl();
    }

    @Test
    @DisplayName("GIVEN mapper registered WHEN getForClass invoked THEN returns that mapper")
    void test() {
        // GIVEN
        StubMapper mapper = Mockito.mock(StubMapper.class);
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
        Assertions.assertThrows(MapperRegistryImpl.MapperNotFound.class, () -> mapperRegistry.getForClass(StubEntity.class));
    }

    @Test
    @DisplayName("GIVEN mapper registered for sub class WHEN getForClass invoked for super class THEN throws")
    void test3() {
        // GIVEN
        StubMapper mapper = Mockito.mock(StubMapper.class);
        mapperRegistry.register(StubEntity.class, mapper);

        // WHEN + THEN
        Assertions.assertThrows(MapperRegistryImpl.MapperNotFound.class, () -> mapperRegistry.getForClass(StubSuperEntity.class));
    }

    @Test
    @DisplayName("GIVEN mapper registered for super class WHEN getForClass invoked for sub class THEN throws")
    void test4() {
        // GIVEN
        StubSuperMapper mapper = Mockito.mock(StubSuperMapper.class);
        mapperRegistry.register(StubSuperEntity.class, mapper);

        // WHEN + THEN
        Assertions.assertThrows(MapperRegistryImpl.MapperNotFound.class, () -> mapperRegistry.getForClass(StubEntity .class));
    }

    static class StubEntity implements Entity<String> {
        @Override
        public String getId() {
            return null;
        }
    }

    interface StubSpecification extends Specification<StubEntity> { }

    interface StubMapper extends DataMapper<StubEntity, String, StubSpecification> { }

    static class StubSuperEntity extends StubEntity {
        @Override
        public String getId() {
            return null;
        }
    }

    interface StubSuperSpecification extends Specification<StubSuperEntity> { }

    interface StubSuperMapper extends DataMapper<StubSuperEntity, String, StubSuperSpecification> { }
}
