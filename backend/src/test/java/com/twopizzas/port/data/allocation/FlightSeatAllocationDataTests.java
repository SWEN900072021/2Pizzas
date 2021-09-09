package com.twopizzas.port.data.allocation;

import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import com.twopizzas.port.data.passenger.PassengerMapper;
import com.twopizzas.port.data.seat.FlightSeatMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationResultsMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationResultsMapperImpl;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationsForFlightBookingLoader;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationsForFlightLoader;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
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
    @DisplayName("GIVEN valid seat allocation object and passenger and seat in database WHEN get all allocations for flight invoked THEN flight allocations returned")
    void test() {
        // GIVEN
        // flight and seat ids to search for
        EntityId flightId = EntityId.nextId();

        EntityId seatId = EntityId.nextId();
        FlightSeat flightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeat.getId()).thenReturn(seatId);
        Mockito.doReturn(flightSeat).when(seatMapper).read(Mockito.eq(seatId));


        EntityId seatIdOther = EntityId.nextId();
        FlightSeat flightSeatOther = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeatOther.getId()).thenReturn(seatIdOther);
        Mockito.doReturn(flightSeatOther).when(seatMapper).read(Mockito.eq(seatIdOther));

        insertTestFlight(flightId, seatId, seatIdOther);

        // other flight id that should not be found
        EntityId flightIdOther = EntityId.nextId();

        EntityId flightSeatIdOtherFlight = EntityId.nextId();
        FlightSeat flightSeatOtherFlight = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeatOtherFlight.getId()).thenReturn(flightSeatIdOtherFlight);
        Mockito.doReturn(flightSeatOtherFlight).when(seatMapper).read(Mockito.eq(flightSeatIdOtherFlight));

        insertTestFlight(flightIdOther, flightSeatIdOtherFlight);

        // booking id
        EntityId bookingId = EntityId.nextId();
        EntityId passengerId = EntityId.nextId();
        Passenger passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getId()).thenReturn(passengerId);
        Mockito.doReturn(passenger).when(passengerMapper).read(Mockito.eq(passengerId));

        insertTestBooking(bookingId, flightId, passengerId);

        // allocate both seats on flight
        FlightSeatAllocation entity = new FlightSeatAllocation(
                flightSeat, passenger
        );
        insertTestAllocation(entity);

        FlightSeatAllocation entityOther = new FlightSeatAllocation(
                flightSeatOther, passenger
        );
        insertTestAllocation(entityOther);

        // add an allocation for a seat that is not the flight being searched for
        insertTestAllocation(new FlightSeatAllocation(
                flightSeatOtherFlight, passenger
        ));

        // WHEN
        FlightSeatAllocationsForFlightLoader loader = new FlightSeatAllocationsForFlightLoader(connectionPool, mapper, flightId);
        ValueHolder<List<FlightSeatAllocation>> flightAllocations = loader.load();

        // THEN
        Assertions.assertNotNull(flightAllocations);
        Assertions.assertNotNull(flightAllocations.get());

        Assertions.assertEquals(2, flightAllocations.get().size());
        Assertions.assertTrue(flightAllocations.get().stream().map(FlightSeatAllocation::getSeat).collect(Collectors.toList()).contains(entity.getSeat()));
        Assertions.assertTrue(flightAllocations.get().stream().map(FlightSeatAllocation::getSeat).collect(Collectors.toList()).contains(entityOther.getSeat()));
        Assertions.assertTrue(flightAllocations.get().stream().map(FlightSeatAllocation::getPassenger).collect(Collectors.toSet()).contains(entityOther.getPassenger()));
    }

    @Test
    @DisplayName("GIVEN flight seat in database WHEN delete invoked THEN flight seat removed from database")
    void test2() {
        // GIVEN
        EntityId flightId = EntityId.nextId();

        EntityId seatId = EntityId.nextId();
        FlightSeat flightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeat.getId()).thenReturn(seatId);
        Mockito.doReturn(flightSeat).when(seatMapper).read(Mockito.eq(seatId));

        EntityId seatIdOther = EntityId.nextId();
        FlightSeat flightSeatOther = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeatOther.getId()).thenReturn(seatIdOther);
        Mockito.doReturn(flightSeatOther).when(seatMapper).read(Mockito.eq(seatIdOther));

        EntityId seatIdOtherOther = EntityId.nextId();
        FlightSeat flightSeatOtherOther = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeatOtherOther.getId()).thenReturn(seatIdOtherOther);
        Mockito.doReturn(flightSeatOtherOther).when(seatMapper).read(Mockito.eq(seatIdOtherOther));

        insertTestFlight(flightId, seatId, seatIdOther, seatIdOtherOther);

        // other flight id that should not be found
        EntityId flightIdOther = EntityId.nextId();

        EntityId flightSeatIdOtherFlight = EntityId.nextId();
        FlightSeat flightSeatOtherFlight = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeatOtherFlight.getId()).thenReturn(flightSeatIdOtherFlight);
        Mockito.doReturn(flightSeatOtherFlight).when(seatMapper).read(Mockito.eq(flightSeatIdOtherFlight));

        insertTestFlight(flightIdOther, flightSeatIdOtherFlight);

        // booking to search for
        EntityId bookingId = EntityId.nextId();
        EntityId passengerId = EntityId.nextId();
        Passenger passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getId()).thenReturn(passengerId);
        Mockito.doReturn(passenger).when(passengerMapper).read(Mockito.eq(passengerId));

        insertTestBooking(bookingId, flightId, passengerId);

        // other booking that should not be returned
        EntityId bookingIdOther = EntityId.nextId();
        EntityId passengerIdOtherBooking = EntityId.nextId();
        Passenger passengerOtherBooking = Mockito.mock(Passenger.class);
        Mockito.when(passengerOtherBooking.getId()).thenReturn(passengerIdOtherBooking);
        Mockito.doReturn(passengerOtherBooking).when(passengerMapper).read(Mockito.eq(passengerIdOtherBooking));
        insertTestBooking(bookingIdOther, flightId, passengerIdOtherBooking);

        FlightSeatAllocation entity = new FlightSeatAllocation(
                flightSeat, passenger
        );
        insertTestAllocation(entity);

        FlightSeatAllocation entityOther = new FlightSeatAllocation(
                flightSeatOther, passenger
        );
        insertTestAllocation(entityOther);

        // add allocation for other booking
        insertTestAllocation(new FlightSeatAllocation(
                flightSeatOtherOther, passengerOtherBooking
        ));

        // allocate passenger to other flight
        insertTestAllocation(new FlightSeatAllocation(
                flightSeatOtherFlight, passenger
        ));

        // WHEN
        FlightSeatAllocationsForFlightBookingLoader loader = new FlightSeatAllocationsForFlightBookingLoader(connectionPool, mapper, flightId, bookingId);
        ValueHolder<List<FlightSeatAllocation>> bookingAllocations = loader.load();

        // THEN
        Assertions.assertNotNull(bookingAllocations);
        Assertions.assertNotNull(bookingAllocations.get());

        Assertions.assertEquals(2, bookingAllocations.get().size());
        Assertions.assertTrue(bookingAllocations.get().stream().map(FlightSeatAllocation::getSeat).collect(Collectors.toList()).contains(entity.getSeat()));
        Assertions.assertTrue(bookingAllocations.get().stream().map(FlightSeatAllocation::getSeat).collect(Collectors.toList()).contains(entityOther.getSeat()));
        Assertions.assertTrue(bookingAllocations.get().stream().map(FlightSeatAllocation::getPassenger).collect(Collectors.toSet()).contains(entityOther.getPassenger()));
    }

    private void insertTestFlight(EntityId flightId, EntityId... seatIds) {
        insertTestFlight(flightId);
        Arrays.stream(seatIds).forEach(s -> insertTestSeat(s, flightId));
    }

    private void insertTestBooking(EntityId bookingId, EntityId flightId, EntityId... passengerIds) {
        insertTestBooking(bookingId, flightId);
        Arrays.stream(passengerIds).forEach(p -> insertTestPassenger(p, bookingId));
    }

    private void insertTestAllocation(FlightSeatAllocation allocation) {
        new SqlStatement("INSERT INTO seatAllocation (seatId, passengerId) VALUES (?, ?);", allocation.getSeat().getId().toString(), allocation.getPassenger().getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestFlight(EntityId flightId) {
        new SqlStatement("INSERT INTO flight (id) VALUES (?);", flightId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestSeat(EntityId seatId, EntityId flightId) {
        new SqlStatement("INSERT INTO seat (id, flightId) VALUES (?, ?);", seatId.toString(), flightId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestBooking(EntityId bookingId, EntityId flightId) {
        new SqlStatement("INSERT INTO booking (id, flightId) VALUES (?, ?);", bookingId.toString(), flightId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestPassenger(EntityId passengerId, EntityId bookingId) {
        new SqlStatement("INSERT INTO passenger (id, bookingId) VALUES (?, ?);", passengerId.toString(), bookingId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }
}