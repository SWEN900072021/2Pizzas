package com.twopizzas.port.data.allocation;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.domain.flight.SeatClass;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import com.twopizzas.port.data.passenger.PassengerMapper;
import com.twopizzas.port.data.seat.AllSeatsForFlightSpecification;
import com.twopizzas.port.data.seat.FlightSeatMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationResultsMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationResultsMapperImpl;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationsForFlightLoader;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FlightSeatAllocationDataTests {
    private FlightSeatAllocationResultsMapper mapper;

    @Mock
    private FlightSeatMapper seatMapper;

    @Mock
    private PassengerMapper passengerMapper;

    private ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);

        mapper = new FlightSeatAllocationResultsMapperImpl(passengerMapper, seatMapper);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid seat allocation object and booking and seat in database WHEN create invoked THEN seat allocation persisted in database")
    void test() {
//        // GIVEN
//        EntityId flightId = EntityId.nextId();
//        EntityId bookingId = EntityId.nextId();
//        EntityId seatId = EntityId.nextId();
//        FlightSeat flightSeat = Mockito.mock(FlightSeat.class);
//        Mockito.when(flightSeat.getId()).thenReturn(seatId);
//
//        insertTestSeat(flightSeat.getId().toString(), flightId.toString());
//
//        EntityId passengerId = EntityId.nextId();
//        Passenger passenger = Mockito.mock(Passenger.class);
//        Mockito.when(passenger.getId()).thenReturn(passengerId);
//
//        insertTestSeat(passenger.getId().toString(), bookingId.toString());
//
//        FlightSeatAllocation entity = new FlightSeatAllocation(
//                flightSeat, passenger
//        );
//
//        Mockito.when(seatMapper.read(Mockito.eq(seatId))).thenReturn(flightSeat);
//        Mockito.when(passengerMapper.read(Mockito.eq(passengerId))).thenReturn(passenger);
//
//        // WHEN
//        FlightSeatAllocationsForFlightLoader loader = new FlightSeatAllocationsForFlightLoader(connectionPool, mapper, flightId);
//
//        // THEN
//        FlightSeat persisted = mapper.read(entity.getId());
//        Assertions.assertNotNull(persisted);
//
//        Assertions.assertEquals(entity.getId(), persisted.getId());
//        Assertions.assertEquals(entity.getName(), persisted.getName());
//        Assertions.assertEquals(entity.getFlight(), persisted.getFlight());
//        Mockito.verify(flightMapper).read(Mockito.eq(flightId));
    }

    @Test
    @DisplayName("GIVEN flight seat in database WHEN delete invoked THEN flight seat removed from database")
    void test2() {
//        // GIVEN
//        EntityId flightId = EntityId.nextId();
//        Flight flight = Mockito.mock(Flight.class);
//        Mockito.when(flight.getId()).thenReturn(flightId);
//
//        insertTestFlight(flight.getId().toString());
//
//        FlightSeat entity = new FlightSeat(
//                "1A", SeatClass.FIRST, flight
//        );
//
//        mapper.create(entity);
//
//        // WHEN
//        mapper.delete(entity);
//
//        // THEN
//        FlightSeat gone = mapper.read(entity.getId());
//        Assertions.assertNull(gone);
    }

    @Test
    @DisplayName("GIVEN flight seat in database WHEN update invoked THEN flight seat updated in database")
    void test3() {
//        // GIVEN
//        EntityId flightId = EntityId.nextId();
//        Flight flight = Mockito.mock(Flight.class);
//        Mockito.when(flight.getId()).thenReturn(flightId);
//
//        insertTestFlight(flight.getId().toString());
//
//        Mockito.doReturn(flight).when(flightMapper).read(Mockito.eq(flightId));
//
//        FlightSeat entity = new FlightSeat(
//                "1A", SeatClass.FIRST, flight
//        );
//        mapper.create(entity);
//
//        EntityId flightIdUpdated = EntityId.nextId();
//        Flight flightUpdated = Mockito.mock(Flight.class);
//        Mockito.when(flightUpdated.getId()).thenReturn(flightIdUpdated);
//
//        insertTestFlight(flightUpdated.getId().toString());
//
//        Mockito.doReturn(flightUpdated).when(flightMapper).read(Mockito.eq(flightIdUpdated));
//
//        FlightSeat update = new FlightSeat(
//                entity.getId(), "1B", SeatClass.ECONOMY, BaseValueHolder.of(flightUpdated)
//        );
//
//        // WHEN
//        mapper.update(update);
//
//        // THEN
//        FlightSeat updated = mapper.read(entity.getId());
//        Assertions.assertNotNull(updated);
//
//        Assertions.assertEquals(update.getId(), updated.getId());
//        Assertions.assertEquals(update.getName(), updated.getName());
//        Assertions.assertEquals(update.getFlight(), updated.getFlight());
//        Mockito.verify(flightMapper).read(Mockito.eq(flightIdUpdated));
    }

    @Test
    @DisplayName("GIVEN two flight seats in database for flight WHEN execute AllSeatsForFlightSpecification THEN flight seats returned")
    void test4() {
//        // GIVEN
//        EntityId flightId = EntityId.nextId();
//        Flight flight = Mockito.mock(Flight.class);
//        Mockito.when(flight.getId()).thenReturn(flightId);
//
//        insertTestFlight(flight.getId().toString());
//
//        EntityId otherFlightId = EntityId.nextId();
//        Flight otherFlight = Mockito.mock(Flight.class);
//        Mockito.when(otherFlight.getId()).thenReturn(otherFlightId);
//
//        insertTestFlight(otherFlight.getId().toString());
//
//        FlightSeat entity = new FlightSeat(
//                "1A", SeatClass.FIRST, flight
//        );
//
//        FlightSeat entitySecond = new FlightSeat(
//                "1B", SeatClass.FIRST, flight
//        );
//
//        FlightSeat entityOther = new FlightSeat(
//                "1C", SeatClass.FIRST, otherFlight
//        );
//
//        mapper.create(entity);
//        mapper.create(entitySecond);
//        mapper.create(entityOther);
//
//        // WHEN
//        AllSeatsForFlightSpecification specification = new AllSeatsForFlightSpecification(mapper, flightId);
//        List<FlightSeat> all = mapper.readAll(specification);
//
//        // THEN
//        Assertions.assertNotNull(all);
//        Assertions.assertEquals(2, all.size());
//        Assertions.assertTrue(all.stream().map(FlightSeat::getId).collect(Collectors.toList()).contains(entity.getId()));
//        Assertions.assertTrue(all.stream().map(FlightSeat::getId).collect(Collectors.toList()).contains(entitySecond.getId()));
    }

    private void insertTestSeat(String seatId, String flightId) {
        new SqlStatement("INSERT INTO flight (id) VALUES (?);", flightId).doExecute(connectionPool.getCurrentTransaction());
        new SqlStatement("INSERT INTO seat (id, flightId) VALUES (?, ?);", seatId, flightId).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestPassenger(String passengerId, String bookingId) {
        new SqlStatement("INSERT INTO booking (id) VALUES (?);", bookingId).doExecute(connectionPool.getCurrentTransaction());
        new SqlStatement("INSERT INTO passenger (id, bookingId) VALUES (?, ?);", passengerId, bookingId).doExecute(connectionPool.getCurrentTransaction());
    }
}