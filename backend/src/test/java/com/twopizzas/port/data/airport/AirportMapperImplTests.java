package com.twopizzas.port.data.airport;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.OptimisticLockingException;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class AirportMapperImplTests {

    private AirportMapperImpl mapper;
    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        mapper = new AirportMapperImpl(connectionPool);
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
        Airport entity = new Airport(
                "COD",  "New Test Airport", "Berlin", ZoneId.of("Asia/Calcutta")
        );

        // WHEN
        mapper.create(entity);

        // THEN
        Airport persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getCode(), persisted.getCode());
        Assertions.assertEquals(entity.getName(), persisted.getName());
        Assertions.assertEquals(entity.getLocation(), persisted.getLocation());
        Assertions.assertEquals(entity.getUtcOffset(), persisted.getUtcOffset());
        Assertions.assertEquals(entity.getStatus(), persisted.getStatus());
    }

    @Test
    @DisplayName("GIVEN airport in database WHEN delete invoked THEN airport removed from database")
    void test2() {
        // GIVEN
        Airport entity = new Airport(
                "COD",  "New Test Airport", "Berlin", ZoneId.of("Asia/Calcutta")
        );
        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        Airport gone = mapper.read(entity.getId());
        Assertions.assertNull(gone);
    }

    @Test
    @DisplayName("GIVEN airport in database WHEN update invoked THEN airport updated in database")
    void test3() {
        // GIVEN
        Airport entity = new Airport(
                EntityId.nextId(), "COD",  "New Test Airport", "Berlin", ZoneId.of("Asia/Calcutta"), Airport.AirportStatus.ACTIVE, 0
        );
        mapper.create(entity);

        Airport update = new Airport(
                entity.getId(), "CED", "Updated Test Airport", "France", ZoneId.of("Europe/Berlin"), Airport.AirportStatus.INACTIVE, 0
        );

        // WHEN
        mapper.update(update);

        // THEN
        Airport updated = mapper.read(entity.getId());
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(update.getId(), updated.getId());
        Assertions.assertEquals(update.getCode(), updated.getCode());
        Assertions.assertEquals(update.getName(), updated.getName());
        Assertions.assertEquals(update.getLocation(), updated.getLocation());
        Assertions.assertEquals(update.getUtcOffset(), updated.getUtcOffset());
        Assertions.assertEquals(update.getStatus(), updated.getStatus());
    }

    @Test
    @DisplayName("GIVEN two airports in database WHEN execute AllAirportsSpecification THEN airports returned")
    void test4() {
        // GIVEN
        Airport entity = new Airport(
                "COD",  "New Test Airport", "Berlin", ZoneId.of("Asia/Calcutta")
        );

        Airport entitySecond = new Airport(
                "COD",  "New Test Airport", "Moon", ZoneId.of("Asia/Calcutta")
        );

        mapper.create(entity);
        mapper.create(entitySecond);

        // WHEN
        AllAirportsSpecification specification = new AllAirportsSpecification(mapper);
        List<Airport> all = mapper.readAll(specification);

        // THEN
        Assertions.assertNotNull(all);
        Assertions.assertTrue(all.size() >= 2);
        Assertions.assertTrue(all.stream().map(Airport::getId).collect(Collectors.toList()).contains(entity.getId()));
        Assertions.assertTrue(all.stream().map(Airport::getId).collect(Collectors.toList()).contains(entitySecond.getId()));
    }

    @Test
    @DisplayName("GIVEN airport version is invalid WHEN update invoked THEN throw OptimisticLockingException and airport not updated")
    void test5() {
        // GIVEN
        Airport airport = new Airport(
                EntityId.nextId(), "COD",  "New Test Airport", "Berlin", ZoneId.of("Asia/Calcutta"),
                Airport.AirportStatus.ACTIVE, 0
        );
        mapper.create(airport);

        Airport update = new Airport(
                airport.getId(), "CED", "Updated Test Airport", "France", ZoneId.of("Europe/Berlin"),
                Airport.AirportStatus.INACTIVE, airport.getVersion() + 1
        );

        // WHEN
        Assertions.assertThrows(OptimisticLockingException.class, () -> mapper.update(update));

        // THEN
        Airport updated = mapper.read(airport.getId());
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(airport.getId(), updated.getId());
        Assertions.assertEquals(airport.getCode(), updated.getCode());
        Assertions.assertEquals(airport.getName(), updated.getName());
        Assertions.assertEquals(airport.getLocation(), updated.getLocation());
        Assertions.assertEquals(airport.getUtcOffset(), updated.getUtcOffset());
        Assertions.assertEquals(airport.getStatus(), updated.getStatus());
    }
}
