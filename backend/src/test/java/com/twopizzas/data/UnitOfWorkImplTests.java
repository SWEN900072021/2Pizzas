package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UnitOfWorkImplTests {

    private UnitOfWork unitOfWork;

    @Mock
    private DataSource dataSource;

    @Mock
    private DataMapperRegistry dataMapperRegistry;

    @Mock
    private DataMapper<StubEntity, String, StubSpecification> dataMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        unitOfWork = new UnitOfWorkImpl(dataSource, dataMapperRegistry);
    }

    @Test
    @DisplayName("GIVEN no registrations WHEN commit invoked THEN do nothing")
    void test() {
        // GIVEN
        // nothing registered

        // WHEN
        unitOfWork.commit();

        // THEN
        Mockito.verify(dataSource, Mockito.never()).startNewTransaction();
        Mockito.verify(dataMapperRegistry, Mockito.never()).getForClass(Mockito.any());
    }

    @Test
    @DisplayName("GIVEN entities registered as new WHEN commit invoked THEN start transaction invoke create on dataMapper for entity and commit transaction")
    void test2() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerNew(entity);

        Mockito.when(dataMapperRegistry.getForClass(Mockito.eq(StubEntity.class))).thenReturn(dataMapper);

        // WHEN
        unitOfWork.commit();

        // THEN
        InOrder events = Mockito.inOrder(dataSource, dataMapper);
        events.verify(dataMapper).create(Mockito.eq(entity));
        events.verify(dataSource).commitTransaction();
        events.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GIVEN entities registered as dirty WHEN commit invoked THEN start transaction invoke update on dataMapper for entity and commit transaction")
    void test3() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerDirty(entity);

        Mockito.when(dataMapperRegistry.getForClass(Mockito.eq(StubEntity.class))).thenReturn(dataMapper);

        // WHEN
        unitOfWork.commit();

        // THEN
        InOrder events = Mockito.inOrder(dataSource, dataMapper);
        events.verify(dataMapper).update(Mockito.eq(entity));
        events.verify(dataSource).commitTransaction();
        events.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GIVEN entities registered as deleted WHEN commit invoked THEN start transaction invoke delete on dataMapper for entity and commit transaction")
    void test4() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerDeleted(entity);

        Mockito.when(dataMapperRegistry.getForClass(Mockito.eq(StubEntity.class))).thenReturn(dataMapper);

        // WHEN
        unitOfWork.commit();

        // THEN
        InOrder events = Mockito.inOrder(dataSource, dataMapper);
        events.verify(dataMapper).delete(Mockito.eq(entity));
        events.verify(dataSource).commitTransaction();
        events.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GIVEN entities registered as clean WHEN commit invoked THEN do nothing")
    void test5() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerClean(entity);

        // WHEN
        unitOfWork.commit();

        // THEN
        Mockito.verify(dataMapperRegistry, Mockito.never()).getForClass(Mockito.any());
        Mockito.verify(dataSource).commitTransaction();
    }

    @Test
    @DisplayName("GIVEN entities registered as new and then updated WHEN commit invoked THEN start transaction invoke create on dataMapper for entity and commit transaction")
    void test6() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerNew(entity);
        unitOfWork.registerDirty(entity);

        Mockito.when(dataMapperRegistry.getForClass(Mockito.eq(StubEntity.class))).thenReturn(dataMapper);

        // WHEN
        unitOfWork.commit();

        // THEN
        InOrder events = Mockito.inOrder(dataSource, dataMapper);
        events.verify(dataMapper).create(Mockito.eq(entity));
        events.verify(dataSource).commitTransaction();
        events.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GIVEN entities registered as new and then deleted WHEN commit invoked THEN do nothing")
    void test7() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerNew(entity);
        unitOfWork.registerDeleted(entity);

        // WHEN
        unitOfWork.commit();

        // THEN
        Mockito.verify(dataMapperRegistry, Mockito.never()).getForClass(Mockito.any());
        Mockito.verify(dataSource).commitTransaction();
    }

    @Test
    @DisplayName("GIVEN entities registered as clean and then dirty WHEN commit invoked THEN start transaction invoke update on dataMapper for entity and commit transaction")
    void test8() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerClean(entity);
        unitOfWork.registerDirty(entity);

        Mockito.when(dataMapperRegistry.getForClass(Mockito.eq(StubEntity.class))).thenReturn(dataMapper);

        // WHEN
        unitOfWork.commit();

        // THEN
        InOrder events = Mockito.inOrder(dataSource, dataMapper);
        events.verify(dataMapper).update(Mockito.eq(entity));
        events.verify(dataSource).commitTransaction();
        events.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GIVEN entities registered as dirty and then deleted WHEN commit invoked THEN start transaction invoke delete on dataMapper for entity and commit transaction")
    void test9() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerDirty(entity);
        unitOfWork.registerDeleted(entity);

        Mockito.when(dataMapperRegistry.getForClass(Mockito.eq(StubEntity.class))).thenReturn(dataMapper);

        // WHEN
        unitOfWork.commit();

        // THEN
        InOrder events = Mockito.inOrder(dataSource, dataMapper);
        events.verify(dataMapper).delete(Mockito.eq(entity));
        events.verify(dataSource).commitTransaction();
        events.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GIVEN entities registered as dirty WHEN register new THEN throws")
    void test10() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerDirty(entity);

        // WHEN + THEN
        Assertions.assertThrows(DataConsistencyViolation.class, () -> unitOfWork.registerNew(entity));
    }

    @Test
    @DisplayName("GIVEN entities registered as deleted WHEN register new THEN throws")
    void test11() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerDeleted(entity);

        // WHEN + THEN
        Assertions.assertThrows(DataConsistencyViolation.class, () -> unitOfWork.registerNew(entity));
    }

    @Test
    @DisplayName("GIVEN entities registered as deleted WHEN register dirty THEN throws")
    void test12() {
        // GIVEN
        StubEntity entity = new StubEntity("someId");
        unitOfWork.registerDeleted(entity);

        // WHEN + THEN
        Assertions.assertThrows(DataConsistencyViolation.class, () -> unitOfWork.registerDirty(entity));
    }
}
