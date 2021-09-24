package com.twopizzas.port.data.airline;

import com.twopizzas.api.airline.AirlineDto;
import com.twopizzas.api.airline.AirlineMapper;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;
import org.mapstruct.factory.Mappers;

import java.sql.SQLException;
import java.util.List;

public class AllAirlineSpecificationTests {

    private AirlineMapperImpl mapper;
    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        mapper = new AirlineMapperImpl(connectionPool);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid Airlines in database WHEN specification executed THEN returns all airlines")
    void test() {
        // GIVEN
        Airline entity = new Airline("username", "password", "qantas", "QN");
        Airline entityOther = new Airline("username", "password", "qantas", "QN");

        mapper.create(entityOther);
        mapper.create(entity);

        // WHEN
        List<Airline> airlines = mapper.readAll(new AllAirlinesSpecification(mapper));

        // THEN
        Assertions.assertFalse(airlines.isEmpty());
        Assertions.assertTrue(airlines.contains(entity));
        Assertions.assertTrue(airlines.contains(entityOther));
    }
}
