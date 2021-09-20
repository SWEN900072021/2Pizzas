package com.twopizzas.port.data.flight;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.booking.TimePeriod;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.port.data.DataTestConfig;
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
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class FlightSearchSpecificationImplTests {
    private FlightMapper mapper;

    @Mock
    private AirportMapper airportMapper;

    @Mock
    private AirlineMapper airlineMapper;

    @Mock
    private FlightSeatMapper flightSeatMapper;

    @Mock
    private AirplaneProfileMapper airplaneProfileMapper;

    private FlightSeatAllocationMapper flightSeatAllocationMapper;

    @Mock
    private PassengerMapper passengerMapper;

    private ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    private AirplaneProfile profile;
    private Airline airline;
    private Airline airlineOther;
    private Airport origin;
    private Airport destination;

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

        EntityId airlineId = EntityId.nextId();
        airline = Mockito.mock(Airline.class);
        Mockito.when(airline.getId()).thenReturn(airlineId);
        insertTestAirline(airline.getId());
        Mockito.doReturn(airline).when(airlineMapper).read(Mockito.eq(airlineId));

        EntityId airlineIdOther = EntityId.nextId();
        airlineOther = Mockito.mock(Airline.class);
        Mockito.when(airlineOther.getId()).thenReturn(airlineIdOther);
        insertTestAirline(airlineOther.getId());
        Mockito.doReturn(airlineOther).when(airlineMapper).read(Mockito.eq(airlineIdOther));

        origin = Mockito.mock(Airport.class);
        EntityId originId = EntityId.nextId();
        Mockito.when(origin.getId()).thenReturn(originId);

        destination = Mockito.mock(Airport.class);
        EntityId destinationId = EntityId.nextId();
        Mockito.when(destination.getId()).thenReturn(destinationId);
        insertTestAirports(origin.getId(), destination.getId());
        Mockito.doReturn(origin).when(airportMapper).read(Mockito.eq(originId));
        Mockito.doReturn(destination).when(airportMapper).read(Mockito.eq(destinationId));
    }

    @Test
    @DisplayName("GIVEN flight in database with origin id WHEN search by origin THEN returns that flight")
    void test() {
        // GIVEN
        OffsetDateTime departure = OffsetDateTime.now();

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        Flight flightOther = new Flight(
                profile,
                airlineOther,
                destination,
                origin,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        mapper.create(flight);
        mapper.create(flightOther);

        // WHEN
        List<Flight> search = mapper.readAll(new FlightSearchSpecification(
                mapper,
                null,
                origin.getId(),
                null,
                null
        ));

        // THEN
        Assertions.assertNotNull(search);
        Assertions.assertEquals(1, search.size());
        Assertions.assertEquals(flight, search.get(0));
    }

    @Test
    @DisplayName("GIVEN flight in database with destination id WHEN search by destination THEN returns that flight")
    void test2() {
        // GIVEN
        OffsetDateTime departure = OffsetDateTime.now();

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        Flight flightOther = new Flight(
                profile,
                airlineOther,
                destination,
                origin,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        mapper.create(flight);
        mapper.create(flightOther);

        // WHEN
        List<Flight> search = mapper.readAll(new FlightSearchSpecification(
                mapper,
                null,
                null,
                destination.getId(),
                null
        ));

        // THEN
        Assertions.assertNotNull(search);
        Assertions.assertEquals(1, search.size());
        Assertions.assertEquals(flight, search.get(0));
    }

    @Test
    @DisplayName("GIVEN flight in database WHEN search by airline THEN returns airline flights")
    void test3() {
        // GIVEN
        OffsetDateTime departure = OffsetDateTime.now();

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        Flight flightOther = new Flight(
                profile,
                airlineOther,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        mapper.create(flight);
        mapper.create(flightOther);

        // WHEN
        List<Flight> search = mapper.readAll(new FlightSearchSpecification(
                mapper,
                airline.getId(),
                null,
                null,
                null
                ));

        // THEN
        Assertions.assertNotNull(search);
        Assertions.assertEquals(1, search.size());
        Assertions.assertEquals(flight, search.get(0));
    }

    @Test
    @DisplayName("GIVEN flight in database WHEN search by departure THEN returns departure flights")
    void test4() {
        // GIVEN
        OffsetDateTime departure = OffsetDateTime.now();

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        Flight flightOther = new Flight(
                profile,
                airlineOther,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure.plus(2, ChronoUnit.HOURS),
                departure.plus(4, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        mapper.create(flight);
        mapper.create(flightOther);

        // WHEN
        List<Flight> search = mapper.readAll(new FlightSearchSpecification(
                mapper,
                null,
                null,
                null,
                new TimePeriod(departure.minus(1, ChronoUnit.HOURS), departure.plus(1, ChronoUnit.HOURS))
        ));

        // THEN
        Assertions.assertNotNull(search);
        Assertions.assertEquals(1, search.size());
        Assertions.assertEquals(flight, search.get(0));
    }

    @Test
    @DisplayName("GIVEN flight in database WHEN search by multiple criteria THEN returns flight")
    void test5() {
        // GIVEN
        OffsetDateTime departure = OffsetDateTime.now();

        Flight flight = new Flight(
                profile,
                airline,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure,
                departure.plus(2, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        Flight flightOther = new Flight(
                profile,
                airlineOther,
                origin,
                destination,
                Collections.emptyList(),
                "code",
                departure.plus(2, ChronoUnit.HOURS),
                departure.plus(4, ChronoUnit.HOURS),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        mapper.create(flight);
        mapper.create(flightOther);

        // WHEN
        List<Flight> search = mapper.readAll(new FlightSearchSpecification(
                mapper,
                airline.getId(),
                origin.getId(),
                destination.getId(),
                new TimePeriod(departure.minus(1, ChronoUnit.HOURS), departure.plus(1, ChronoUnit.HOURS))
        ));

        // THEN
        Assertions.assertNotNull(search);
        Assertions.assertEquals(1, search.size());
        Assertions.assertEquals(flight, search.get(0));
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
}
