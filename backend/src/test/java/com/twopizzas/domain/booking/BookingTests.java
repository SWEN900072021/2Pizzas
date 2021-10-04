package com.twopizzas.domain.booking;

import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.flight.*;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class BookingTests {

    @Test
    @DisplayName("GIVEN valid booking WHEN get total cost THEN returns cost of all seats")
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

        Flight returnFlight = Mockito.spy(new Flight(
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
        SeatBooking seatBooking = new SeatBooking(flight, new HashSet<>(Arrays.asList(
                new FlightSeatAllocation(flight.getSeats().stream().filter(s -> s.getSeatClass().equals(SeatClass.FIRST)).findFirst().get(), passenger),
                new FlightSeatAllocation(flight.getSeats().stream().filter(s -> s.getSeatClass().equals(SeatClass.BUSINESS)).findFirst().get(), passenger),
                new FlightSeatAllocation(flight.getSeats().stream().filter(s -> s.getSeatClass().equals(SeatClass.ECONOMY)).findFirst().get(), passenger)
        )));

        SeatBooking returnSeatBooking = new SeatBooking(returnFlight, new HashSet<>(Arrays.asList(
                new FlightSeatAllocation(returnFlight.getSeats().stream().filter(s -> s.getSeatClass().equals(SeatClass.FIRST)).findFirst().get(), passenger),
                new FlightSeatAllocation(returnFlight.getSeats().stream().filter(s -> s.getSeatClass().equals(SeatClass.BUSINESS)).findFirst().get(), passenger),
                new FlightSeatAllocation(returnFlight.getSeats().stream().filter(s -> s.getSeatClass().equals(SeatClass.ECONOMY)).findFirst().get(), passenger)
        )));

        Customer customer = new Customer("string", "string2", "string3", "string4", "string5");
        Booking booking = new Booking(customer);
        booking.addFlight(seatBooking);
        booking.addReturnFlight(returnSeatBooking);

        // WHEN
        BigDecimal cost = booking.getTotalCost();

        // THEN
        Assertions.assertEquals(BigDecimal.valueOf(64), cost);
    }
}
