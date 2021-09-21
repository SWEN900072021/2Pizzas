package com.twopizzas.port.data.seat;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.SeatClass;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import com.twopizzas.port.data.flight.FlightMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FlightSeatMapperImplTests {

    private FlightSeatMapperImpl mapper;

    @Mock
    private FlightMapper flightMapper;

    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);

        mapper = new FlightSeatMapperImpl(flightMapper, connectionPool);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid flight seat object and flight in database WHEN create invoked THEN flight seat persisted in database")
    void test() {
        // GIVEN
        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        FlightSeat entity = new FlightSeat(
                "1A",  SeatClass.FIRST, flight
        );

        Mockito.when(flightMapper.read(Mockito.eq(flightId))).thenReturn(flight);

        // WHEN
        mapper.create(entity);

        // THEN
        FlightSeat persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);

        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getName(), persisted.getName());
        Assertions.assertEquals(entity.getFlight(), persisted.getFlight());
        Mockito.verify(flightMapper).read(Mockito.eq(flightId));
    }

    @Test
    @DisplayName("GIVEN flight seat in database WHEN delete invoked THEN flight seat removed from database")
    void test2() {
        // GIVEN
        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        FlightSeat entity = new FlightSeat(
                "1A",  SeatClass.FIRST, flight
        );

        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        FlightSeat gone = mapper.read(entity.getId());
        Assertions.assertNull(gone);
    }

    @Test
    @DisplayName("GIVEN flight seat in database WHEN update invoked THEN flight seat updated in database")
    void test3() {
        // GIVEN
        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        Mockito.doReturn(flight).when(flightMapper).read(Mockito.eq(flightId));

        FlightSeat entity = new FlightSeat(
                "1A",  SeatClass.FIRST, flight
        );
        mapper.create(entity);

        EntityId flightIdUpdated = EntityId.nextId();
        Flight flightUpdated = Mockito.mock(Flight.class);
        Mockito.when(flightUpdated.getId()).thenReturn(flightIdUpdated);

        insertTestFlight(flightUpdated.getId().toString());

        Mockito.doReturn(flightUpdated).when(flightMapper).read(Mockito.eq(flightIdUpdated));

        FlightSeat update = new FlightSeat(
                entity.getId(), "1B",  SeatClass.ECONOMY, BaseValueHolder.of(flightUpdated)
        );

        // WHEN
        mapper.update(update);

        // THEN
        FlightSeat updated = mapper.read(entity.getId());
        Assertions.assertNotNull(updated);

        Assertions.assertEquals(update.getId(), updated.getId());
        Assertions.assertEquals(update.getName(), updated.getName());
        Assertions.assertEquals(update.getFlight(), updated.getFlight());
        Mockito.verify(flightMapper).read(Mockito.eq(flightIdUpdated));
    }

    @Test
    @DisplayName("GIVEN two flight seats in database for flight WHEN execute AllSeatsForFlightSpecification THEN flight seats returned")
    void test4() {
        // GIVEN
        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId otherFlightId = EntityId.nextId();
        Flight otherFlight = Mockito.mock(Flight.class);
        Mockito.when(otherFlight.getId()).thenReturn(otherFlightId);

        insertTestFlight(otherFlight.getId().toString());

        FlightSeat entity = new FlightSeat(
                "1A",  SeatClass.FIRST, flight
        );

        FlightSeat entitySecond = new FlightSeat(
                "1B",  SeatClass.FIRST, flight
        );

        FlightSeat entityOther = new FlightSeat(
                "1C",  SeatClass.FIRST, otherFlight
        );

        mapper.create(entity);
        mapper.create(entitySecond);
        mapper.create(entityOther);

        // WHEN
        AllSeatsForFlightSpecification specification = new AllSeatsForFlightSpecification(mapper, flightId);
        List<FlightSeat> all = mapper.readAll(specification);

        // THEN
        Assertions.assertNotNull(all);
        Assertions.assertEquals(2, all.size());
        Assertions.assertTrue(all.stream().map(FlightSeat::getId).collect(Collectors.toList()).contains(entity.getId()));
        Assertions.assertTrue(all.stream().map(FlightSeat::getId).collect(Collectors.toList()).contains(entitySecond.getId()));
    }

    private void insertTestFlight(String flightId) {
        new SqlStatement("INSERT INTO flight (id) VALUES (?);", flightId).doExecute(connectionPool.getCurrentTransaction());
    }
}
