package com.twopizzas.port.data.booking;

import com.twopizzas.domain.booking.Booking;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import com.twopizzas.port.data.flight.FlightMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Set;

public class BookingMapperImplTests {

    private BookingMapperImpl mapper;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private FlightMapper flightMapper;

    @Mock
    private FlightSeatAllocationMapper flightSeatAllocationMapper;

    private ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);

        mapper = new BookingMapperImpl(connectionPool, customerMapper, flightMapper, flightSeatAllocationMapper);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() { connectionPool.rollbackTransaction(); }

    @Test
    @DisplayName("GIVEN valid booking, customer, flight, and flight seat object in database WHEN create invoked THEN booking persisted in database")
    void testValidCreate() {

        // GIVEN
        EntityId customerId = EntityId.nextId();
        Customer customer = Mockito.mock(Customer.class);
        Mockito.when(customer.getId()).thenReturn(customerId);

        insertTestUser(customer.getId().toString());
        insertTestCustomer(customer.getId().toString());

        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId returnFlightId = EntityId.nextId();
        Flight returnFlight = Mockito.mock(Flight.class);
        Mockito.when(returnFlight.getId()).thenReturn(returnFlightId);

        insertTestFlight(returnFlight.getId().toString());

        EntityId flightSeatId = EntityId.nextId();
        FlightSeat flightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeat.getId()).thenReturn(flightSeatId);

        insertTestFlightSeat(flightSeat.getId().toString(), flight.getId().toString());

        EntityId returnFlightSeatId = EntityId.nextId();
        FlightSeat returnFlightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(returnFlightSeat.getId()).thenReturn(returnFlightSeatId);

        insertTestFlightSeat(returnFlightSeat.getId().toString(), returnFlight.getId().toString());

        FlightSeatAllocation flightSeatAllocation = Mockito.mock(FlightSeatAllocation.class);
        FlightSeatAllocation returnFlightSeatAllocation = Mockito.mock(FlightSeatAllocation.class);

        Set<FlightSeatAllocation> allocationSet = Mockito.mock(Set.class);
        allocationSet.add(flightSeatAllocation);
        allocationSet.add(returnFlightSeatAllocation);

        SeatBooking flightBooking = new SeatBooking(flight, allocationSet);
        SeatBooking returnFlightBooking = new SeatBooking(flight, allocationSet);

        Booking entity = new Booking(EntityId.nextId(), OffsetDateTime.now().withNano(0), new BigDecimal("100.0"), customer);
        entity.addFlight(flightBooking);
        entity.addReturnFlight(returnFlightBooking);

        Mockito.when(customerMapper.read(Mockito.eq(customerId))).thenReturn(customer);
        Mockito.when(flightMapper.read(Mockito.eq(flightId))).thenReturn(flight);
        Mockito.when(flightMapper.read(Mockito.eq(returnFlightId))).thenReturn(returnFlight);

        // WHEN
        mapper.create(entity);

        // THEN
        Booking persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);

        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getDate().toInstant(), persisted.getDate().toInstant());
        Assertions.assertEquals(entity.getTotalCost(), persisted.getTotalCost());
        Assertions.assertEquals(entity.getCustomer(), persisted.getCustomer());
        Assertions.assertEquals(entity.getFlightReservation().getFlight(), persisted.getFlightReservation().getFlight());
        Assertions.assertEquals(entity.getReturnFlightReservation().getFlight(), persisted.getReturnFlightReservation().getFlight());

        Mockito.verify(customerMapper).read(Mockito.eq(customerId));

    }
    @Test
    @DisplayName("GIVEN valid booking without return flight, customer, flight, and flight seat object in database WHEN create invoked THEN booking persisted in database")
    void test() {

        // GIVEN
        EntityId customerId = EntityId.nextId();
        Customer customer = Mockito.mock(Customer.class);
        Mockito.when(customer.getId()).thenReturn(customerId);

        insertTestUser(customer.getId().toString());
        insertTestCustomer(customer.getId().toString());

        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId flightSeatId = EntityId.nextId();
        FlightSeat flightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeat.getId()).thenReturn(flightSeatId);

        insertTestFlightSeat(flightSeat.getId().toString(), flight.getId().toString());

        FlightSeatAllocation flightSeatAllocation = Mockito.mock(FlightSeatAllocation.class);

        Set<FlightSeatAllocation> allocationSet = Mockito.mock(Set.class);
        allocationSet.add(flightSeatAllocation);

        SeatBooking flightBooking = new SeatBooking(flight, allocationSet);

        Booking entity = new Booking(EntityId.nextId(), OffsetDateTime.now().withNano(0), new BigDecimal("100.0"), customer);
        entity.addFlight(flightBooking);

        Mockito.when(customerMapper.read(Mockito.eq(customerId))).thenReturn(customer);
        Mockito.when(flightMapper.read(Mockito.eq(flightId))).thenReturn(flight);

        // WHEN
        mapper.create(entity);

        // THEN
        Booking persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);

        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getDate().toInstant(), persisted.getDate().toInstant());
        Assertions.assertEquals(entity.getTotalCost(), persisted.getTotalCost());
        Assertions.assertEquals(entity.getCustomer(), persisted.getCustomer());
        Assertions.assertEquals(entity.getFlightReservation().getFlight(), persisted.getFlightReservation().getFlight());
        Assertions.assertNull(persisted.getReturnFlightReservation());

        Mockito.verify(customerMapper).read(Mockito.eq(customerId));
    }

    @Test
    @DisplayName("GIVEN valid booking, customer, flight, and flight seat object in database WHEN update invoked THEN booking updated in database")
    void testValidUpdate() {

        // GIVEN
        EntityId customerId = EntityId.nextId();
        Customer customer = Mockito.mock(Customer.class);
        Mockito.when(customer.getId()).thenReturn(customerId);

        insertTestUser(customer.getId().toString());
        insertTestCustomer(customer.getId().toString());

        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId returnFlightId = EntityId.nextId();
        Flight returnFlight = Mockito.mock(Flight.class);
        Mockito.when(returnFlight.getId()).thenReturn(returnFlightId);

        insertTestFlight(returnFlight.getId().toString());

        EntityId flightSeatId = EntityId.nextId();
        FlightSeat flightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeat.getId()).thenReturn(flightSeatId);

        insertTestFlightSeat(flightSeat.getId().toString(), flight.getId().toString());

        EntityId returnFlightSeatId = EntityId.nextId();
        FlightSeat returnFlightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(returnFlightSeat.getId()).thenReturn(returnFlightSeatId);

        insertTestFlightSeat(returnFlightSeat.getId().toString(), returnFlight.getId().toString());

        FlightSeatAllocation flightSeatAllocation = Mockito.mock(FlightSeatAllocation.class);
        FlightSeatAllocation returnFlightSeatAllocation = Mockito.mock(FlightSeatAllocation.class);

        Set<FlightSeatAllocation> allocationSet = Mockito.mock(Set.class);
        allocationSet.add(flightSeatAllocation);
        allocationSet.add(returnFlightSeatAllocation);

        SeatBooking flightBooking = new SeatBooking(flight, allocationSet);
        SeatBooking returnFlightBooking = new SeatBooking(flight, allocationSet);

        Booking entity = new Booking(EntityId.nextId(), OffsetDateTime.now().withNano(0), new BigDecimal("100.0"), customer);
        entity.addFlight(flightBooking);
        entity.addReturnFlight(returnFlightBooking);

        Mockito.when(customerMapper.read(Mockito.eq(customerId))).thenReturn(customer);
        Mockito.when(flightMapper.read(Mockito.eq(flightId))).thenReturn(flight);
        Mockito.when(flightMapper.read(Mockito.eq(returnFlightId))).thenReturn(returnFlight);

        mapper.create(entity);

        EntityId customerIdUpdated = EntityId.nextId();
        Customer customerUpdated = Mockito.mock(Customer.class);
        Mockito.when(customerUpdated.getId()).thenReturn(customerIdUpdated);

        insertTestUser(customerUpdated.getId().toString());
        insertTestCustomer(customerUpdated.getId().toString());

        EntityId flightIdUpdated = EntityId.nextId();
        Flight flightUpdated = Mockito.mock(Flight.class);
        Mockito.when(flightUpdated.getId()).thenReturn(flightIdUpdated);

        insertTestFlight(flightUpdated.getId().toString());

        EntityId returnFlightIdUpdated = EntityId.nextId();
        Flight returnFlightUpdated = Mockito.mock(Flight.class);
        Mockito.when(returnFlightUpdated.getId()).thenReturn(returnFlightIdUpdated);

        insertTestFlight(returnFlightUpdated.getId().toString());

        EntityId flightSeatIdUpdated = EntityId.nextId();
        FlightSeat flightSeatUpdated = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeatUpdated.getId()).thenReturn(flightSeatIdUpdated);

        insertTestFlightSeat(flightSeatUpdated.getId().toString(), flightUpdated.getId().toString());

        EntityId returnFlightSeatIdUpdated = EntityId.nextId();
        FlightSeat returnFlightSeatUpdated = Mockito.mock(FlightSeat.class);
        Mockito.when(returnFlightSeatUpdated.getId()).thenReturn(returnFlightSeatIdUpdated);

        insertTestFlightSeat(returnFlightSeatUpdated.getId().toString(), returnFlightUpdated.getId().toString());

        FlightSeatAllocation flightSeatAllocationUpdated = Mockito.mock(FlightSeatAllocation.class);
        FlightSeatAllocation returnFlightSeatAllocationUpdated = Mockito.mock(FlightSeatAllocation.class);

        Set<FlightSeatAllocation> allocationSetUpdated = Mockito.mock(Set.class);
        allocationSetUpdated.add(flightSeatAllocationUpdated);
        allocationSetUpdated.add(returnFlightSeatAllocationUpdated);

        SeatBooking flightBookingUpdated = new SeatBooking(flightUpdated, allocationSetUpdated);
        SeatBooking returnFlightBookingUpdated = new SeatBooking(flightUpdated, allocationSetUpdated);

        Booking update = new Booking(entity.getId(), OffsetDateTime.now().withNano(0), new BigDecimal("930.00"), customerUpdated);
        update.addFlight(flightBookingUpdated);
        update.addReturnFlight(returnFlightBookingUpdated);

        Mockito.when(customerMapper.read(Mockito.eq(customerIdUpdated))).thenReturn(customerUpdated);
        Mockito.when(flightMapper.read(Mockito.eq(flightIdUpdated))).thenReturn(flightUpdated);
        Mockito.when(flightMapper.read(Mockito.eq(returnFlightIdUpdated))).thenReturn(returnFlightUpdated);

        // WHEN
        mapper.update(update);

        // THEN
        Booking updated = mapper.read(entity.getId());
        Assertions.assertNotNull(updated);

        Assertions.assertEquals(update.getId(), updated.getId());
        Assertions.assertEquals(update.getDate().toInstant(), updated.getDate().toInstant());
        Assertions.assertEquals(update.getTotalCost(), updated.getTotalCost());
        Assertions.assertEquals(update.getCustomer(), updated.getCustomer());
        Assertions.assertEquals(update.getFlightReservation().getFlight(), updated.getFlightReservation().getFlight());
        Assertions.assertEquals(update.getReturnFlightReservation().getFlight(), updated.getReturnFlightReservation().getFlight());

        Mockito.verify(customerMapper).read(Mockito.eq(customerIdUpdated));


    }

    @Test
    @DisplayName("GIVEN valid booking, customer, flight, and flight seat object in database WHEN delete invoked THEN booking removed from database")
    void testValidDelete() {

        // GIVEN
        EntityId customerId = EntityId.nextId();
        Customer customer = Mockito.mock(Customer.class);
        Mockito.when(customer.getId()).thenReturn(customerId);

        insertTestUser(customer.getId().toString());
        insertTestCustomer(customer.getId().toString());

        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId returnFlightId = EntityId.nextId();
        Flight returnFlight = Mockito.mock(Flight.class);
        Mockito.when(returnFlight.getId()).thenReturn(returnFlightId);

        insertTestFlight(returnFlight.getId().toString());

        EntityId flightSeatId = EntityId.nextId();
        FlightSeat flightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(flightSeat.getId()).thenReturn(flightSeatId);

        insertTestFlightSeat(flightSeat.getId().toString(), flight.getId().toString());

        EntityId returnFlightSeatId = EntityId.nextId();
        FlightSeat returnFlightSeat = Mockito.mock(FlightSeat.class);
        Mockito.when(returnFlightSeat.getId()).thenReturn(returnFlightSeatId);

        insertTestFlightSeat(returnFlightSeat.getId().toString(), returnFlight.getId().toString());

        FlightSeatAllocation flightSeatAllocation = Mockito.mock(FlightSeatAllocation.class);
        FlightSeatAllocation returnFlightSeatAllocation = Mockito.mock(FlightSeatAllocation.class);

        Set<FlightSeatAllocation> allocationSet = Mockito.mock(Set.class);
        allocationSet.add(flightSeatAllocation);
        allocationSet.add(returnFlightSeatAllocation);

        SeatBooking flightBooking = new SeatBooking(flight, allocationSet);
        SeatBooking returnFlightBooking = new SeatBooking(flight, allocationSet);

        Booking entity = new Booking(EntityId.nextId(), OffsetDateTime.now().withNano(0), new BigDecimal("100.0"), customer);
        entity.addFlight(flightBooking);
        entity.addReturnFlight(returnFlightBooking);

        Mockito.when(customerMapper.read(Mockito.eq(customerId))).thenReturn(customer);
        Mockito.when(flightMapper.read(Mockito.eq(flightId))).thenReturn(flight);
        Mockito.when(flightMapper.read(Mockito.eq(returnFlightId))).thenReturn(returnFlight);

        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        Booking removed = mapper.read(entity.getId());
        Assertions.assertNull(removed);

    }


    private void insertTestFlightSeat(String flightSeatId, String flightId) {
        new SqlStatement("INSERT INTO seat (id, flightid) VALUES (?, ?);", flightSeatId, flightId).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestFlight(String flightId) {
        new SqlStatement("INSERT INTO flight (id) VALUES (?);", flightId).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestCustomer(String customerId) {
        new SqlStatement("INSERT INTO customer (id) VALUES (?);", customerId).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestUser(String userId) {
        new SqlStatement("INSERT INTO \"user\" (id) VALUES (?);", userId).doExecute(connectionPool.getCurrentTransaction());
    }

}
