package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DataProxyTests {

    @Mock
    private StubMapper wrappedMapper;

    @Mock
    private UnitOfWork unitOfWork;

    @Mock
    private IdentityMapper identityMapper;

    private  DataMapper<StubEntity, String, StubSpecification> dataMapper;

    private DataProxy<StubEntity, String> proxy;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        proxy = new DataProxy<>(wrappedMapper, identityMapper, unitOfWork);
        dataMapper = (DataMapper<StubEntity, String, StubSpecification>) Proxy.newProxyInstance(wrappedMapper.getClass().getClassLoader(), wrappedMapper.getClass().getInterfaces(), proxy);
    }

    @Test
    @DisplayName("GIVEN wrapped mapper WHEN create invoked THEN added to identityMapper and registered with unitOfWork")
    void test() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        Mockito.when(identityMapper.testAndGet(Mockito.any())).thenReturn(entity);
        Mockito.when(wrappedMapper.getEntityClass()).thenReturn(StubEntity.class);

        // WHEN
        dataMapper.create(entity);

        // THEN
        Mockito.verify(identityMapper).testAndGet(Mockito.refEq(entity));
        Mockito.verify(unitOfWork).registerNew(Mockito.refEq(entity));
        Mockito.verify(wrappedMapper, Mockito.never()).create(Mockito.any());
    }

    @Test
    @DisplayName("GIVEN wrapped mapper WHEN update invoked THEN added to identityMapper and registered with unitOfWork")
    void test2() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        Mockito.when(identityMapper.testAndGet(Mockito.any())).thenReturn(entity);
        Mockito.when(wrappedMapper.getEntityClass()).thenReturn(StubEntity.class);

        // WHEN
        dataMapper.update(entity);

        // THEN
        Mockito.verify(identityMapper).testAndGet(Mockito.refEq(entity));
        Mockito.verify(unitOfWork).registerDirty(Mockito.refEq(entity));
        Mockito.verify(wrappedMapper, Mockito.never()).update(Mockito.any());
    }

    @Test
    @DisplayName("GIVEN wrapped mapper WHEN delete invoked THEN added to identityMapper and registered with unitOfWork")
    void test3() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        Mockito.when(wrappedMapper.getEntityClass()).thenReturn(StubEntity.class);
        Mockito.when(identityMapper.testAndGet(Mockito.any())).thenReturn(entity);

        // WHEN
        dataMapper.delete(entity);

        // THEN
        Mockito.verify(unitOfWork).registerDeleted(Mockito.refEq(entity));
        Mockito.verify(identityMapper).markGone(Mockito.refEq(entity));
        Mockito.verify(wrappedMapper, Mockito.never()).delete(Mockito.any());
    }

    @Test
    @DisplayName("GIVEN wrapped mapper and entity not in identity mapper WHEN read invoked THEN entity read from wrapped mapper, added to identityMapper and returned")
    void test4() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        Mockito.when(identityMapper.get(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(identityMapper.testAndGet(Mockito.any())).thenAnswer(args -> args.getArgument(0));
        Mockito.when(wrappedMapper.read(Mockito.eq(entity.getId()))).thenReturn(entity);
        Mockito.when(wrappedMapper.getEntityClass()).thenReturn(StubEntity.class);

        // WHEN
        StubEntity read = dataMapper.read(entity.getId());

        // THEN
        Mockito.verify(identityMapper).get(Mockito.eq(StubEntity.class), Mockito.eq(entity.getId()));
        Mockito.verify(wrappedMapper).read(Mockito.eq(entity.getId()));
        Mockito.verify(identityMapper).testAndGet(Mockito.refEq(entity));
        Assertions.assertSame(entity, read);
    }

    @Test
    @DisplayName("GIVEN wrapped mapper and entity not in identity mapper and not in database WHEN read invoked THEN entity read from wrapped mapper and not added to identityMapper")
    void test5() {
        // GIVEN
        String id = "someId";
        Mockito.when(identityMapper.get(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(identityMapper.testAndGet(Mockito.any())).thenAnswer(args -> args.getArgument(0));
        Mockito.when(wrappedMapper.read(Mockito.any())).thenReturn(null);
        Mockito.when(wrappedMapper.getEntityClass()).thenReturn(StubEntity.class);

        // WHEN
        StubEntity read = dataMapper.read(id);

        // THEN
        Mockito.verify(identityMapper).get(Mockito.eq(StubEntity.class), Mockito.eq(id));
        Mockito.verify(wrappedMapper).read(Mockito.eq(id));
        Mockito.verify(identityMapper, Mockito.never()).testAndGet(Mockito.any());
        Assertions.assertNull(read);
    }

    @Test
    @DisplayName("GIVEN wrapped mapper and entity in identity mapper WHEN read invoked THEN entity retrieved from identityMapper and returned")
    void test6() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        Mockito.when(identityMapper.get(Mockito.eq(StubEntity.class), Mockito.eq(entity.getId()))).thenAnswer(args -> Optional.of(entity));
        Mockito.when(wrappedMapper.getEntityClass()).thenReturn(StubEntity.class);

        // WHEN
        StubEntity read = dataMapper.read(entity.getId());

        // THEN
        Mockito.verify(identityMapper).get(Mockito.eq(StubEntity.class), Mockito.eq(entity.getId()));
        Mockito.verify(wrappedMapper, Mockito.never()).read(Mockito.any());
        Assertions.assertSame(entity, read);
    }

    @Test
    @DisplayName("GIVEN wrapped mapper and entity in identity mapper WHEN readAll invoked THEN readAll invoked on wrapped mapper and return is filtered for entities in identity mapper")
    void test7() {
        // GIVEN
        StubSpecification spec = Mockito.mock(StubSpecification.class);
        StubEntity entityInMapper = new StubEntity("someId");
        StubEntity entityInDatabase = new StubEntity("someOtherId");
        StubEntity entityInMapperDatabaseResult = new StubEntity(entityInMapper.getId());
        Mockito.when(identityMapper.testAndGet(Mockito.any())).thenAnswer(args -> {
            StubEntity arg = args.getArgument(0);
            if (entityInMapper.getId().equals(arg.getId())) {
                return entityInMapper;
            }
            return arg;
        });
        Mockito.when(wrappedMapper.readAll(Mockito.any())).thenReturn(Arrays.asList(
                entityInMapperDatabaseResult, // return a new object from the database, should switch for what is in mapper
                entityInDatabase));
        Mockito.when(wrappedMapper.getEntityClass()).thenReturn(StubEntity.class);

        // WHEN
        List<StubEntity> results = dataMapper.readAll(spec);

        // THEN
        Mockito.verify(wrappedMapper).readAll(Mockito.refEq(spec));
        Mockito.verify(identityMapper).testAndGet(Mockito.refEq(entityInMapper));
        Mockito.verify(identityMapper).testAndGet(Mockito.refEq(entityInMapperDatabaseResult));
        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
        Assertions.assertSame(entityInMapper, results.get(0));
        Assertions.assertSame(entityInDatabase, results.get(1));
    }
}
