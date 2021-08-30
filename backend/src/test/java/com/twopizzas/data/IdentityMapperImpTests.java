package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class IdentityMapperImpTests {

    IdentityMapper<StubEntity, String> mapper;

    @BeforeEach
    void setup() {
        mapper = new GenericIdentityMapperImpl<>(StubEntity.class);
    }

    @Test
    @DisplayName("GIVEN no entity in mapper WHEN get entity by class and id THEN returns empty")
    void test() {
        // GIVEN
        // map empty

        // WHEN
        Optional<StubEntity> maybeEntity = mapper.get(StubEntity.class, "someId");

        // THEN
        Assertions.assertFalse(maybeEntity.isPresent());
    }

    @Test
    @DisplayName("GIVEN entity in mapper WHEN get entity by class and wrong id THEN returns empty")
    void test1() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        mapper.testAndGet(entity);

        // WHEN
        Optional<StubEntity> maybeEntity = mapper.get(StubEntity.class, "someOtherId");

        // THEN
        Assertions.assertFalse(maybeEntity.isPresent());
    }

    @Test
    @DisplayName("GIVEN no entity in mapper WHEN testAndGet THEN returns entity and puts in mapper")
    void test2() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");

        // WHEN
        StubEntity put = mapper.testAndGet(entity);

        // THEN
        Assertions.assertEquals(entity, put);
        Assertions.assertTrue(mapper.get(StubEntity.class, entity.getId()).isPresent());
        Assertions.assertEquals(entity, mapper.get(StubEntity.class, entity.getId()).get());
    }

    @Test
    @DisplayName("GIVEN entity in mapper WHEN get entity by class and id THEN returns entity")
    void test3() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        mapper.testAndGet(entity);

        // WHEN
        Optional<StubEntity> maybeEntity = mapper.get(StubEntity.class, entity.getId());

        // THEN
        Assertions.assertTrue(maybeEntity.isPresent());
        Assertions.assertEquals(entity, maybeEntity.get());
        Assertions.assertSame(entity, maybeEntity.get());
    }

    @Test
    @DisplayName("GIVEN entity in mapper WHEN testAndGet entity THEN returns stored entity")
    void test4() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        mapper.testAndGet(entity);

        // WHEN
        StubEntity same = Mockito.mock(StubEntity.class);
        Mockito.when(same.getId()).thenReturn("someId");
        StubEntity retrieved = mapper.testAndGet(same);

        // THEN
        Assertions.assertNotSame(entity, same);
        Assertions.assertSame(entity, retrieved);
    }

    @Test
    @DisplayName("GIVEN entity in mapper WHEN testAndGet entity of assignable class THEN returns stored entity")
    void test5() {
        // GIVEN
        StubSubEntity entity = new StubSubEntity("someId");
        mapper.testAndGet(entity);

        // WHEN
        Optional<StubEntity>  retrieved = mapper.get(StubEntity.class, entity.getId());

        // THEN
        Assertions.assertTrue(retrieved.isPresent());
        Assertions.assertSame(entity, retrieved.get());
    }

    @Test
    @DisplayName("GIVEN entity in mapper WHEN testAndGet entity of assignable class THEN returns stored entity")
    void test6() {
        // GIVEN
        StubSubEntity entity = new StubSubEntity("someId");
        StubEntity superEntity = new StubEntity("someId");
        mapper.testAndGet(entity);

        // WHEN
        StubEntity retrieved = mapper.testAndGet(superEntity);

        // THEN
        Assertions.assertSame(entity, retrieved);
    }

    @Test
    @DisplayName("GIVEN entity marked gone in mapper WHEN testAndGet THEN throws")
    void test7() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        mapper.testAndGet(entity);
        mapper.markGone(entity);

        // WHEN + THEN
        Assertions.assertThrows(DataConsistencyViolation.class, () -> mapper.testAndGet(entity));
    }

    @Test
    @DisplayName("GIVEN entity marked gone in mapper WHEN get THEN throws")
    void test8() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        mapper.testAndGet(entity);
        mapper.markGone(entity);

        // WHEN + THEN
        Assertions.assertThrows(DataConsistencyViolation.class, () -> mapper.get(StubEntity.class, entity.getId()));
    }
}
