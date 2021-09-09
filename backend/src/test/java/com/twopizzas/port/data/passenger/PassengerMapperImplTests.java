package com.twopizzas.port.data.passenger;

import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.booking.BookingMapper;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import com.twopizzas.port.data.flight.FlightMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.time.LocalDate;

public class PassengerMapperImplTests {

    private PassengerMapperImpl mapper;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private FlightMapper flightMapper;

    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);

        mapper = new PassengerMapperImpl(connectionPool, bookingMapper);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() { connectionPool.rollbackTransaction(); }

    @Test
    @DisplayName("GIVEN valid passenger, booking, and flight in database WHEN create invoked THEN passenger persisted in database")
    void testValidCreate() {
        // GIVEN
        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId bookingId = EntityId.nextId();
        Booking booking = Mockito.mock(Booking.class);
        Mockito.when(booking.getId()).thenReturn(bookingId);

        insertTestBooking(booking.getId().toString(), flight.getId().toString());

        Passenger entity = new Passenger("John", "Smith", LocalDate.now(), "Indonesian", "P123", booking);

        Mockito.when(bookingMapper.read(Mockito.eq(bookingId))).thenReturn(booking);

        // WHEN
        mapper.create(entity);

        // THEN
        Passenger persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);

        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getGivenName(), persisted.getGivenName());
        Assertions.assertEquals(entity.getSurname(), persisted.getSurname());
        Assertions.assertEquals(entity.getDateOfBirth(), persisted.getDateOfBirth());
        Assertions.assertEquals(entity.getNationality(), persisted.getNationality());
        Assertions.assertEquals(entity.getPassportNumber(), persisted.getPassportNumber());
        Assertions.assertEquals(entity.getBooking(), persisted.getBooking());

        Mockito.verify(bookingMapper).read(Mockito.eq(bookingId));

    }

    @Test
    @DisplayName("GIVEN valid passenger, booking, and flight in database WHEN update invoked THEN passenger updated in database")
    void testValidUpdate() {
        // GIVEN
        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId bookingId = EntityId.nextId();
        Booking booking = Mockito.mock(Booking.class);
        Mockito.when(booking.getId()).thenReturn(bookingId);

        insertTestBooking(booking.getId().toString(), flight.getId().toString());
        Mockito.doReturn(booking).when(bookingMapper).read(Mockito.eq(bookingId));

        Passenger entity = new Passenger("John", "Smith", LocalDate.now(), "Indonesian", "P123", booking);

        mapper.create(entity);

        EntityId flightIdUpdated = EntityId.nextId();
        Flight flightUpdated = Mockito.mock(Flight.class);
        Mockito.when(flightUpdated.getId()).thenReturn(flightIdUpdated);

        insertTestFlight(flightUpdated.getId().toString());

        EntityId bookingIdUpdated = EntityId.nextId();
        Booking bookingUpdated = Mockito.mock(Booking.class);
        Mockito.when(bookingUpdated.getId()).thenReturn(bookingIdUpdated);

        insertTestBooking(bookingUpdated.getId().toString(), flightUpdated.getId().toString());
        Mockito.doReturn(bookingUpdated).when(bookingMapper).read(Mockito.eq(bookingIdUpdated));

        Passenger update = new Passenger(entity.getId(), "Jane", "Vulpix", LocalDate.now(), "Australian", "ABCD", bookingUpdated);

        // WHEN
        mapper.update(update);

        // THEN
        Passenger updated = mapper.read(entity.getId());
        Assertions.assertNotNull(updated);

        Assertions.assertEquals(update.getId(), updated.getId());
        Assertions.assertEquals(update.getGivenName(), updated.getGivenName());
        Assertions.assertEquals(update.getSurname(), updated.getSurname());
        Assertions.assertEquals(update.getDateOfBirth(), updated.getDateOfBirth());
        Assertions.assertEquals(update.getNationality(), updated.getNationality());
        Assertions.assertEquals(update.getPassportNumber(), updated.getPassportNumber());
        Assertions.assertEquals(update.getBooking(), updated.getBooking());

        Mockito.verify(bookingMapper).read(Mockito.eq(bookingIdUpdated));

    }

    @Test
    @DisplayName("GIVEN valid passenger, booking, and flight in database WHEN delete invoked THEN passenger removed from database")
    void testValidDelete() {

        // GIVEN
        EntityId flightId = EntityId.nextId();
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getId()).thenReturn(flightId);

        insertTestFlight(flight.getId().toString());

        EntityId bookingId = EntityId.nextId();
        Booking booking = Mockito.mock(Booking.class);
        Mockito.when(booking.getId()).thenReturn(bookingId);

        insertTestBooking(booking.getId().toString(), flight.getId().toString());

        Passenger entity = new Passenger("John", "Smith", LocalDate.now(), "Indonesian", "P123", booking);

        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        Passenger removed = mapper.read(entity.getId());
        Assertions.assertNull(removed);

    }

    private void insertTestFlight(String flightId) {
        new SqlStatement("INSERT INTO flight (id) VALUES (?);", flightId).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestBooking(String bookingId, String flightId) {
        new SqlStatement("INSERT INTO booking (id, flightid) VALUES (?, ?);", bookingId, flightId).doExecute(connectionPool.getCurrentTransaction());
    }

}
