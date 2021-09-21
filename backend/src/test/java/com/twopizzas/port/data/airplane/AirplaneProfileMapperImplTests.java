package com.twopizzas.port.data.airplane;

import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AirplaneProfileMapperImplTests {
    private AirplaneProfileMapperImpl mapper;
    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        mapper = new AirplaneProfileMapperImpl(connectionPool);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid airport object WHEN create invoked THEN airport persisted in database")
    void test() {
        // GIVEN
        AirplaneProfile entity = new AirplaneProfile(
                "Airbus", "someType", 1, 2, 3,  4,  5, 6
        );

        // WHEN
        mapper.create(entity);

        // THEN
        AirplaneProfile persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getCode(), persisted.getCode());
        Assertions.assertEquals(entity.getType(), persisted.getType());
        Assertions.assertEquals(entity.getFirstClassColumns(), persisted.getFirstClassColumns());
        Assertions.assertEquals(entity.getFirstClassRows(), persisted.getFirstClassRows());
        Assertions.assertEquals(entity.getBusinessClassColumns(), persisted.getBusinessClassColumns());
        Assertions.assertEquals(entity.getBusinessClassRows(), persisted.getBusinessClassRows());
        Assertions.assertEquals(entity.getEconomyClassColumns(), persisted.getEconomyClassColumns());
        Assertions.assertEquals(entity.getEconomyClassRows(), persisted.getEconomyClassRows());
    }

    @Test
    @DisplayName("GIVEN airport in database WHEN delete invoked THEN airport removed from database")
    void test2() {
        // GIVEN
        AirplaneProfile entity = new AirplaneProfile(
                "Airbus", "someType", 1, 2, 3,  4,  5, 6
        );
        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        AirplaneProfile gone = mapper.read(entity.getId());
        Assertions.assertNull(gone);
    }

    @Test
    @DisplayName("GIVEN airport in database WHEN update invoked THEN airport updated in database")
    void test3() {
        // GIVEN
        AirplaneProfile entity = new AirplaneProfile(
                "Airbus", "someType", 1, 2, 3,  4,  5, 6
        );
        mapper.create(entity);

        // GIVEN
        AirplaneProfile update = new AirplaneProfile(
                entity.getId(), "Boeing", "someOtherType", 7, 8, 9,  10,  11, 12
        );
        // WHEN
        mapper.update(update);

        // THEN
        AirplaneProfile updated = mapper.read(entity.getId());
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(update.getId(), updated.getId());
        Assertions.assertEquals(update.getCode(), updated.getCode());
        Assertions.assertEquals(update.getType(), updated.getType());
        Assertions.assertEquals(update.getFirstClassColumns(), updated.getFirstClassColumns());
        Assertions.assertEquals(update.getFirstClassRows(), updated.getFirstClassRows());
        Assertions.assertEquals(update.getBusinessClassColumns(), updated.getBusinessClassColumns());
        Assertions.assertEquals(update.getBusinessClassRows(), updated.getBusinessClassRows());
        Assertions.assertEquals(update.getEconomyClassColumns(), updated.getEconomyClassColumns());
        Assertions.assertEquals(update.getEconomyClassRows(), updated.getEconomyClassRows());
    }

    @Test
    @DisplayName("GIVEN two airports in database WHEN execute AllAirportsSpecification THEN airports returned")
    void test4() {
        // GIVEN
        AirplaneProfile entity = new AirplaneProfile(
                "Airbus", "someType", 1, 2, 3,  4,  5, 6
        );

        AirplaneProfile entitySecond = new AirplaneProfile(
                "Airbus", "someType", 1, 2, 3,  4,  5, 6
        );

        mapper.create(entity);
        mapper.create(entitySecond);

        // WHEN
        AirplaneProfileSpecification specification = new AllAirplaneProfilesSpecification(mapper);
        List<AirplaneProfile> all = mapper.readAll(specification);

        // THEN
        Assertions.assertNotNull(all);
        Assertions.assertTrue(all.size() >= 2);
        Assertions.assertTrue(all.stream().map(AirplaneProfile::getId).collect(Collectors.toList()).contains(entity.getId()));
        Assertions.assertTrue(all.stream().map(AirplaneProfile::getId).collect(Collectors.toList()).contains(entitySecond.getId()));
    }
}
