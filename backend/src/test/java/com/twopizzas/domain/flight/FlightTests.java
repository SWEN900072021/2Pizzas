package com.twopizzas.domain.flight;

import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.domain.error.BusinessRuleException;
import com.twopizzas.domain.user.Airline;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class FlightTests {

    @Test
    @DisplayName("GIVEN flight has vacant seats WHEN allocate vacant seat THEN returns allocations")
    void test() {
        // GIVEN
        OffsetDateTime now = OffsetDateTime.now();
        Flight flight = Mockito.spy(new Flight(
                new AirplaneProfile("name", "code", 1, 2, 3, 4, 5, 6),
                new Airline("someUsername", "somePassword", "someName", "someCode"),
                new Airport("COD", "name", "location", ZoneId.of("UTC+01:00")),
                new Airport("COA", "otherName", "otherLocation", ZoneId.of("UTC+02:00")),
                Collections.singletonList(
                        new StopOver(new Airport("COD", "name", "location", ZoneId.of("UTC+01:00")), now.plus(1, ChronoUnit.HOURS), now.plus(2, ChronoUnit.HOURS))
                ),
                "code",
                now,
                now.plus(3, ChronoUnit.HOURS),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(2)
        ));

        Passenger passenger = new Passenger("string", "string2", LocalDate.now(), "string3", "string4");

        FlightSeat seat = flight.getSeats().iterator().next();
        BookingRequest request = new BookingRequest(Collections.singletonList(new BookingRequest.SeatAllocationRequest(
                seat.getName(),
                passenger
        )));

        // WHEN
        SeatBooking allocations = flight.allocateSeats(request);

        // THEN
        Assertions.assertEquals(1, allocations.getAllocations().size());
        Assertions.assertEquals(passenger, allocations.getAllocations().iterator().next().getPassenger());
        Assertions.assertEquals(seat, allocations.getAllocations().iterator().next().getSeat());

        Assertions.assertEquals(1, flight.getAllocatedSeats().size());
        Assertions.assertEquals(passenger, flight.getAllocatedSeats().iterator().next().getPassenger());
        Assertions.assertEquals(seat, flight.getAllocatedSeats().iterator().next().getSeat());
    }

    @Test
    @DisplayName("GIVEN flight has no vacant seats WHEN allocate booked seat THEN throws business error")
    void test2() {
        // GIVEN
        OffsetDateTime now = OffsetDateTime.now();
        Flight flight = Mockito.spy(new Flight(
                new AirplaneProfile("name", "code", 1, 2, 3, 4, 5, 6),
                new Airline("someUsername", "somePassword", "someName", "someCode"),
                new Airport("COD", "name", "location", ZoneId.of("UTC+01:00")),
                new Airport("COA", "otherName", "otherLocation", ZoneId.of("UTC+02:00")),
                Collections.singletonList(
                        new StopOver(new Airport("COD", "name", "location", ZoneId.of("UTC+01:00")), now.plus(1, ChronoUnit.HOURS), now.plus(2, ChronoUnit.HOURS))
                ),
                "code",
                now,
                now.plus(3, ChronoUnit.HOURS),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(2)
        ));

        Passenger passenger = new Passenger("string", "string2", LocalDate.now(), "string3", "string4");

        FlightSeat seat = flight.getSeats().iterator().next();
        BookingRequest request = new BookingRequest(Collections.singletonList(new BookingRequest.SeatAllocationRequest(
                seat.getName(),
                passenger
        )));

        SeatBooking allocations = flight.allocateSeats(request);

        // WHEN + THEN
        Assertions.assertThrows(BusinessRuleException.class, () -> flight.allocateSeats(request));
    }
}
