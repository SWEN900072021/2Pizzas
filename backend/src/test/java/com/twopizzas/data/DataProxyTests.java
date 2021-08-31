package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Proxy;

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

        // WHEN
        StubEntity created = dataMapper.create(entity);

        // THEN
        Assertions.assertSame(entity, created);
        Mockito.verify(identityMapper).testAndGet(Mockito.eq(entity));
        Mockito.verify(unitOfWork).registerNew(Mockito.eq(entity));
    }
}
