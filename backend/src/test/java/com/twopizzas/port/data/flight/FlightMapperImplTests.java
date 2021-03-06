package com.twopizzas.port.data.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.domain.flight.*;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.OptimisticLockingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.airplane.AirplaneProfileMapper;
import com.twopizzas.port.data.airport.AirportMapper;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import com.twopizzas.port.data.passenger.PassengerMapper;
import com.twopizzas.port.data.seat.FlightSeatMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class FlightMapperImplTests {

    private FlightMapper mapper;

    @Mock
    private AirportMapper airportMapper;

    @Mock
    private AirlineMapper airlineMapper;

    @Mock
    private FlightSeatMapper flightSeatMapper;

    @Mock
    private AirplaneProfileMapper  airplaneProfileMapper;

    private FlightSeatAllocationMapper flightSeatAllocationMapper;

    @Mock
    private PassengerMapper passengerMapper;

    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    private AirplaneProfile profile;
    private Airline airline;
    private Airport origin;
    private Airport destination;
    private FlightSeat seat;
    private Passenger passenger;

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);

        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);

        flightSeatAllocationMapper = new FlightSeatAllocationMapperImpl(passengerMapper, flightSeatMapper);

        mapper = new FlightMapperImpl(connectionPool,
                flightSeatMapper,
                airlineMapper,
                flightSeatAllocationMapper,
                airplaneProfileMapper,
                airportMapper);

        profile = Mockito.mock(AirplaneProfile.class);
        Mockito.when(profile.getId()).thenReturn(EntityId.nextId());
        insertTestAirplaneProfile(profile.getId());
        Mockito.when(airplaneProfileMapper.read(Mockito.any())).thenReturn(profile);

        airline = Mockito.mock(Airline.class);
        Mockito.when(airline.getId()).thenReturn(EntityId.nextId());
        insertTestAirline(airline.getId());
        Mockito.when(airlineMapper.read(Mockito.any())).thenReturn(airline);

        origin = Mockito.mock(Airport.class);
        EntityId originId = EntityId.nextId();
        Mockito.when(origin.getId()).thenReturn(originId);
        destination = Mockito.mock(Airport.class);
        EntityId destinationId = EntityId.nextId();
        Mockito.when(destination.getId()).thenReturn(destinationId);
        insertTestAirports(origin.getId(), destination.getId());
        Mockito.doReturn(origin).when(airportMapper).read(Mockito.eq(originId));
        Mockito.doReturn(destination).when(airportMapper).read(Mockito.eq(destinationId));

        seat = Mockito.mock(FlightSeat.class);
        Mockito.when(profile.getFlightSeats(Mockito.any())).thenReturn(Collections.singletonList(seat));
        Mockito.when(seat.getId()).thenReturn(EntityId.nextId());
        Mockito.when(flightSeatMapper.read(Mockito.eq(seat.getId()))).thenReturn(seat);

        passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getId()).thenReturn(EntityId.nextId());
        Mockito.when(passengerMapper.read(Mockito.eq(passenger.getId()))).thenReturn(passenger);
    }

    @Test
    @DisplayName("GIVEN valid flight WHEN create invoked THEN flight persisted in database")
    void test() {
        // GIVEN
        Airport stopOverLocation = Mockito.mock(Airport.class);
        EntityId stopOverLocationId = EntityId.nextId();
        Mockito.when(stopOverLocation.getId()).thenReturn(stopOverLocationId);
        Mockito.doReturn(stopOverLocation).when(airportMapper).read(Mockito.eq(stopOverLocationId));
        insertTestAirports(stopOverLocation.getId());

        Mockito.when(flightSeatMapper.readAll(Mockito.any())).thenReturn(Collections.singletonList(seat));

        OffsetDateTime departure = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).withNano(0);
        OffsetDateTime arrival = departure.plus(12, ChronoUnit.HOURS);

        BigDecimal firstClassCost = new BigDecimal(100);
        BigDecimal businessClassCost = new BigDecimal(80);
        BigDecimal economyClassCost = new BigDecimal(40);

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.singletonList(new StopOver(
                        stopOverLocation,
                        departure.plus(6, ChronoUnit.HOURS),
                        departure.plus(7, ChronoUnit.HOURS)
                )),
                "code",
                departure,
                arrival,
                firstClassCost,
                businessClassCost,
                economyClassCost
        );

        Assertions.assertNotNull(flight.getStatus());

        // WHEN
        mapper.create(flight);

        // THEN
        Flight created = mapper.read(flight.getId());
        Assertions.assertNotNull(created);
        Assertions.assertEquals(flight.getId(), created.getId());
        Assertions.assertEquals(flight.getCode(), created.getCode());
        Assertions.assertEquals(flight.getDestination(), created.getDestination());
        Assertions.assertEquals(flight.getOrigin(), created.getOrigin());
        Assertions.assertEquals(flight.getAirline(), created.getAirline());
        Assertions.assertEquals(flight.getAirplaneProfile(), created.getAirplaneProfile());
        Assertions.assertEquals(flight.getDeparture(), created.getDeparture());
        Assertions.assertEquals(flight.getArrival(), created.getArrival());
        Assertions.assertEquals(flight.getStatus(), created.getStatus());
        Assertions.assertEquals(flight.getFirstClassCost(), created.getFirstClassCost());
        Assertions.assertEquals(flight.getBusinessClassCost(), created.getBusinessClassCost());
        Assertions.assertEquals(flight.getEconomyClassCost(), created.getEconomyClassCost());
        Assertions.assertNotNull(created.getStopOvers());
        Assertions.assertEquals(1, created.getStopOvers().size());
        Assertions.assertNotNull(created.getStopOvers().get(0));
        Assertions.assertEquals(flight.getStopOvers().get(0).getArrival(), created.getStopOvers().get(0).getArrival());
        Assertions.assertEquals(flight.getStopOvers().get(0).getDeparture(), created.getStopOvers().get(0).getDeparture());
        Assertions.assertEquals(flight.getStopOvers().get(0).getLocation(), created.getStopOvers().get(0).getLocation());
        Assertions.assertNotNull(flight.getSeats());
        Assertions.assertEquals(1, flight.getSeats().size());
        Assertions.assertEquals(seat, flight.getSeats().iterator().next());
    }

    // create airport and airline
    // create flight with same airports and airline with diff versions
    @Test
    @DisplayName("GIVEN airline and airport with invalid version WHEN create invoked THEN throw OptimisticLockingException")
    void test2() {
        // GIVEN
        Airline newAirline = Mockito.mock(Airline.class);
        Mockito.doReturn(airline.getId()).when(newAirline).getId();
        Mockito.doReturn(airline.getVersion() + 1).when(newAirline).getVersion();

        Airport newOrigin = Mockito.mock(Airport.class);
        Mockito.doReturn(origin.getId()).when(newOrigin).getId();
        Mockito.doReturn(origin.getVersion() + 1).when(newOrigin).getVersion();

        Airport newDestination = Mockito.mock(Airport.class);
        Mockito.doReturn(destination.getId()).when(newDestination).getId();
        Mockito.doReturn(destination.getVersion() + 1).when(newDestination).getVersion();

        Airport stopOverLocation = Mockito.mock(Airport.class);
        EntityId stopOverLocationId = EntityId.nextId();
        Mockito.when(stopOverLocation.getId()).thenReturn(stopOverLocationId);
        Mockito.doReturn(stopOverLocation).when(airportMapper).read(Mockito.eq(stopOverLocationId));
        insertTestAirports(stopOverLocation.getId());

        Mockito.when(flightSeatMapper.readAll(Mockito.any())).thenReturn(Collections.singletonList(seat));

        OffsetDateTime departure = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).withNano(0);
        OffsetDateTime arrival = departure.plus(12, ChronoUnit.HOURS);

        BigDecimal firstClassCost = new BigDecimal(100);
        BigDecimal businessClassCost = new BigDecimal(80);
        BigDecimal economyClassCost = new BigDecimal(40);

        Flight flight = new Flight(
                profile,
                newAirline,
                newOrigin,
                newDestination,
                Collections.singletonList(new StopOver(
                        stopOverLocation,
                        departure.plus(6, ChronoUnit.HOURS),
                        departure.plus(7, ChronoUnit.HOURS)
                )),
                "code",
                departure,
                arrival,
                firstClassCost,
                businessClassCost,
                economyClassCost
        );

        Assertions.assertNotNull(flight.getStatus());

        // WHEN + THEN
        Assertions.assertThrows(OptimisticLockingException.class, () -> mapper.create(flight));

        Flight read = mapper.read(flight.getId());
        Assertions.assertNull(read);
    }

    @Test
    @DisplayName("GIVEN valid flight WHEN update invoked THEN flight updated in database")
    void test3() {
        // GIVEN
        Airport stopOverLocation = Mockito.mock(Airport.class);
        EntityId stopOverLocationId = EntityId.nextId();
        Mockito.when(stopOverLocation.getId()).thenReturn(stopOverLocationId);
        Mockito.doReturn(stopOverLocation).when(airportMapper).read(Mockito.eq(stopOverLocationId));
        insertTestAirports(stopOverLocation.getId());

        OffsetDateTime departure = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).withNano(0);
        OffsetDateTime arrival = departure.plus(12, ChronoUnit.HOURS);

        BigDecimal firstClassCost = new BigDecimal(100);
        BigDecimal businessClassCost = new BigDecimal(80);
        BigDecimal economyClassCost = new BigDecimal(40);

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.singletonList(new StopOver(
                        stopOverLocation,
                        departure.plus(6, ChronoUnit.HOURS),
                        departure.plus(7, ChronoUnit.HOURS)
                )),
                "code",
                departure,
                arrival,
                firstClassCost,
                businessClassCost,
                economyClassCost
        );

        mapper.create(flight);

        EntityId bookingId = EntityId.nextId();
        EntityId seatIdOther = EntityId.nextId();
        insertTestSeat(seat.getId(), flight.getId());
        insertTestSeat(seatIdOther, flight.getId());
        insertTestPassenger(passenger.getId(), bookingId, flight.getId());
        insertTestAllocation(passenger.getId(), seatIdOther);

        Airport stopOverLocationUpdate = Mockito.mock(Airport.class);
        EntityId stopOverLocationIdUpdate = EntityId.nextId();
        Mockito.when(stopOverLocationUpdate.getId()).thenReturn(stopOverLocationIdUpdate);
        Mockito.doReturn(stopOverLocationUpdate).when(airportMapper).read(Mockito.eq(stopOverLocationIdUpdate));
        insertTestAirports(stopOverLocationUpdate.getId());

        OffsetDateTime departureUpdate = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).withNano(0);

        BigDecimal firstClassCostUpdate = new BigDecimal(110);
        BigDecimal businessClassCostUpdate = new BigDecimal(90);
        BigDecimal economyClassCostUpdate = new BigDecimal(50);

        Flight update = new Flight(
                flight.getId(),
                BaseValueHolder.of(Collections.singletonList(
                        new FlightSeatAllocation(
                                seat, passenger
                        )
                )),
                profile,
                airline,
                BaseValueHolder.of(Collections.emptyList()),
                origin,
                destination,
                departureUpdate,
                departureUpdate.plus(12, ChronoUnit.HOURS),
                Collections.singletonList(new StopOver(
                        stopOverLocationUpdate,
                        departureUpdate.plus(6, ChronoUnit.HOURS),
                        departureUpdate.plus(7, ChronoUnit.HOURS)
                )),
                "codeUpdate",
                Flight.FlightStatus.CANCELLED,
                firstClassCostUpdate,
                businessClassCostUpdate,
                economyClassCostUpdate,
                flight.getVersion()
        );

        // WHEN
        mapper.update(update);

        // THEN
        Flight updated = mapper.read(flight.getId());
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(update.getId(), updated.getId());
        Assertions.assertEquals(update.getCode(), updated.getCode());
        Assertions.assertEquals(update.getDestination(), updated.getDestination());
        Assertions.assertEquals(update.getOrigin(), updated.getOrigin());
        Assertions.assertEquals(update.getAirline(), updated.getAirline());
        Assertions.assertEquals(update.getAirplaneProfile(), updated.getAirplaneProfile());
        Assertions.assertEquals(update.getDeparture(), updated.getDeparture());
        Assertions.assertEquals(update.getArrival(), updated.getArrival());
        Assertions.assertEquals(update.getStatus(), updated.getStatus());
        Assertions.assertEquals(update.getFirstClassCost(), updated.getFirstClassCost());
        Assertions.assertEquals(update.getBusinessClassCost(), updated.getBusinessClassCost());
        Assertions.assertEquals(update.getEconomyClassCost(), updated.getEconomyClassCost());
        Assertions.assertNotNull(updated.getStopOvers());
        Assertions.assertEquals(1, updated.getStopOvers().size());
        Assertions.assertNotNull(updated.getStopOvers().get(0));
        Assertions.assertEquals(update.getStopOvers().get(0).getArrival(), updated.getStopOvers().get(0).getArrival());
        Assertions.assertEquals(update.getStopOvers().get(0).getDeparture(), updated.getStopOvers().get(0).getDeparture());
        Assertions.assertEquals(update.getStopOvers().get(0).getLocation(), updated.getStopOvers().get(0).getLocation());
        Assertions.assertNotNull(updated.getAllocatedSeats());
        Assertions.assertEquals(1, updated.getAllocatedSeats().size());
        Assertions.assertNotNull(updated.getAllocatedSeats().get(0));
        Assertions.assertEquals(update.getAllocatedSeats().get(0).getSeat(), updated.getAllocatedSeats().get(0).getSeat());
        Assertions.assertEquals(update.getAllocatedSeats().get(0).getPassenger(), updated.getAllocatedSeats().get(0).getPassenger());
        Assertions.assertEquals(update.getVersion() + 1, updated.getVersion());
    }

    @Test
    @DisplayName("GIVEN flight version is invalid WHEN update invoked THEN throw OptimisticLockingException")
    void test4() {
        // GIVEN
        Airport stopOverLocation = Mockito.mock(Airport.class);
        EntityId stopOverLocationId = EntityId.nextId();
        Mockito.when(stopOverLocation.getId()).thenReturn(stopOverLocationId);
        Mockito.doReturn(stopOverLocation).when(airportMapper).read(Mockito.eq(stopOverLocationId));
        insertTestAirports(stopOverLocation.getId());

        OffsetDateTime departure = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).withNano(0);
        OffsetDateTime arrival = departure.plus(12, ChronoUnit.HOURS);

        BigDecimal firstClassCost = new BigDecimal(100);
        BigDecimal businessClassCost = new BigDecimal(80);
        BigDecimal economyClassCost = new BigDecimal(40);

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.singletonList(new StopOver(
                        stopOverLocation,
                        departure.plus(6, ChronoUnit.HOURS),
                        departure.plus(7, ChronoUnit.HOURS)
                )),
                "code",
                departure,
                arrival,
                firstClassCost,
                businessClassCost,
                economyClassCost
        );

        mapper.create(flight);

        EntityId bookingId = EntityId.nextId();
        EntityId seatIdOther = EntityId.nextId();
        insertTestSeat(seat.getId(), flight.getId());
        insertTestSeat(seatIdOther, flight.getId());
        insertTestPassenger(passenger.getId(), bookingId, flight.getId());
        insertTestAllocation(passenger.getId(), seatIdOther);

        Airport stopOverLocationUpdate = Mockito.mock(Airport.class);
        EntityId stopOverLocationIdUpdate = EntityId.nextId();
        Mockito.when(stopOverLocationUpdate.getId()).thenReturn(stopOverLocationIdUpdate);
        insertTestAirports(stopOverLocationUpdate.getId());

        OffsetDateTime departureUpdate = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).withNano(0);

        BigDecimal firstClassCostUpdate = new BigDecimal(110);
        BigDecimal businessClassCostUpdate = new BigDecimal(90);
        BigDecimal economyClassCostUpdate = new BigDecimal(50);

        Flight update = new Flight(
                flight.getId(),
                BaseValueHolder.of(Collections.singletonList(
                        new FlightSeatAllocation(
                                seat, passenger
                        )
                )),
                profile,
                airline,
                BaseValueHolder.of(Collections.emptyList()),
                origin,
                destination,
                departureUpdate,
                departureUpdate.plus(12, ChronoUnit.HOURS),
                Collections.singletonList(new StopOver(
                        stopOverLocationUpdate,
                        departureUpdate.plus(6, ChronoUnit.HOURS),
                        departureUpdate.plus(7, ChronoUnit.HOURS)
                )),
                "codeUpdate",
                Flight.FlightStatus.CANCELLED,
                firstClassCostUpdate,
                businessClassCostUpdate,
                economyClassCostUpdate,
                flight.getVersion() + 1
        );

        // WHEN + THEN
        Assertions.assertThrows(OptimisticLockingException.class, () -> mapper.update(update));

        Flight read = mapper.read(flight.getId());

        Assertions.assertNotNull(read);
        Assertions.assertEquals(flight.getId(), read.getId());
        Assertions.assertEquals(flight.getCode(), read.getCode());
        Assertions.assertEquals(flight.getDestination(), read.getDestination());
        Assertions.assertEquals(flight.getOrigin(), read.getOrigin());
        Assertions.assertEquals(flight.getAirline(), read.getAirline());
        Assertions.assertEquals(flight.getAirplaneProfile(), read.getAirplaneProfile());
        Assertions.assertEquals(flight.getDeparture(), read.getDeparture());
        Assertions.assertEquals(flight.getArrival(), read.getArrival());
        Assertions.assertEquals(flight.getStatus(), read.getStatus());
        Assertions.assertEquals(flight.getFirstClassCost(), read.getFirstClassCost());
        Assertions.assertEquals(flight.getBusinessClassCost(), read.getBusinessClassCost());
        Assertions.assertEquals(flight.getEconomyClassCost(), read.getEconomyClassCost());
        Assertions.assertNotNull(read.getStopOvers());
        Assertions.assertEquals(1, read.getStopOvers().size());
        Assertions.assertNotNull(read.getStopOvers().get(0));
        Assertions.assertEquals(flight.getStopOvers().get(0).getArrival(), read.getStopOvers().get(0).getArrival());
        Assertions.assertEquals(flight.getStopOvers().get(0).getDeparture(), read.getStopOvers().get(0).getDeparture());
        Assertions.assertEquals(flight.getStopOvers().get(0).getLocation(), read.getStopOvers().get(0).getLocation());
        Assertions.assertNotNull(flight.getSeats());
        Assertions.assertEquals(1, flight.getSeats().size());
        Assertions.assertEquals(seat, flight.getSeats().iterator().next());
    }

    @Test
    @DisplayName("GIVEN flight in database WHEN delete invoked THEN flight removed from database")
    void test5() {
        // GIVEN
        Airport stopOverLocation = Mockito.mock(Airport.class);
        EntityId stopOverLocationId = EntityId.nextId();
        Mockito.when(stopOverLocation.getId()).thenReturn(stopOverLocationId);
        Mockito.doReturn(stopOverLocation).when(airportMapper).read(Mockito.eq(stopOverLocationId));
        insertTestAirports(stopOverLocation.getId());

        Mockito.when(flightSeatMapper.readAll(Mockito.any())).thenReturn(Collections.singletonList(seat));

        OffsetDateTime departure = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).withNano(0);
        OffsetDateTime arrival = departure.plus(12, ChronoUnit.HOURS);

        BigDecimal firstClassCost = new BigDecimal(100);
        BigDecimal businessClassCost = new BigDecimal(80);
        BigDecimal economyClassCost = new BigDecimal(40);

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.singletonList(new StopOver(
                        stopOverLocation,
                        departure.plus(6, ChronoUnit.HOURS),
                        departure.plus(7, ChronoUnit.HOURS)
                )),
                "code",
                departure,
                arrival,
                firstClassCost,
                businessClassCost,
                economyClassCost
        );

        mapper.create(flight);

        // WHEN
        mapper.delete(flight);

        // THEN
        Flight gone = mapper.read(flight.getId());
        Assertions.assertNull(gone);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    private void insertTestAirplaneProfile(EntityId profileId) {
        new SqlStatement("INSERT INTO airplaneProfile (id) VALUES (?);", profileId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestAirline(EntityId airlineId) {
        new SqlStatement("INSERT INTO \"user\" (id) VALUES (?);", airlineId.toString()).doExecute(connectionPool.getCurrentTransaction());
        new SqlStatement("INSERT INTO airline (id) VALUES (?);", airlineId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestAirports(EntityId... airportIds) {
        for (EntityId id : airportIds) {
            new SqlStatement("INSERT INTO airport (id) VALUES (?);", id.toString()).doExecute(connectionPool.getCurrentTransaction());
        }
    }

    private void insertTestSeat(EntityId seatId, EntityId flightId) {
        new SqlStatement("INSERT INTO seat (id, flightId) VALUES (?, ?);", seatId.toString(), flightId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestAllocation(EntityId passengerId, EntityId seatId) {
        new SqlStatement("INSERT INTO seatAllocation (passengerId, seatId) VALUES (?, ?);", passengerId.toString(), seatId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertTestPassenger(EntityId passengerId, EntityId bookingId, EntityId flightId) {
        new SqlStatement("INSERT INTO booking (id, flightId) VALUES (?, ?);", bookingId.toString(), flightId.toString()).doExecute(connectionPool.getCurrentTransaction());
        new SqlStatement("INSERT INTO passenger (id, bookingId) VALUES (?, ?);", passengerId.toString(), bookingId.toString()).doExecute(connectionPool.getCurrentTransaction());
    }
}
