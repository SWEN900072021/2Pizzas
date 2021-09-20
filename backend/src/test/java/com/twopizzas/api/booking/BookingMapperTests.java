package com.twopizzas.api.booking;

import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.domain.flight.*;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class BookingMapperTests {

    private BookingMapper mapper = Mappers.getMapper(BookingMapper.class);

    @Test
    @DisplayName("GIVEN valid Booking WHEN mapped THEN returns valid BookingDto")
    void test() {
        // GIVEN
        OffsetDateTime now = OffsetDateTime.now();

        Flight flight = new Flight(
                new AirplaneProfile("name", "code", 1, 2, 3, 4, 5, 6),
                new Airline("someUsername", "somePassword", "someName", "someCode"),
                new Airport("COD", "name", "location", ZoneId.of("UTC+01:00")),
                new Airport("COA", "otherName", "otherLocation", ZoneId.of("UTC+02:00")),
                Collections.singletonList(
                        new StopOver(new Airport("COD", "name", "location", ZoneId.of("UTC+01:00")), now.plus(1, ChronoUnit.HOURS), now.plus(2, ChronoUnit.HOURS))
                ),
                "code",
                now,
                now.plus(3, ChronoUnit.HOURS)
        );

        Passenger passenger = new Passenger("givenName", "surname", LocalDate.now(), "passport", "nationality");

        Booking source = new Booking(
                new Customer("username", "password", "givenName", "surname", "email@email.com")
        );
        source.addFlight(
                new SeatBooking(
                        flight,
                        Collections.singleton(new FlightSeatAllocation(
                                new FlightSeat("1A", SeatClass.FIRST, flight),
                                passenger
                        ))
        ));
        source.addReturnFlight(
                new SeatBooking(
                        flight,
                        Collections.singleton(new FlightSeatAllocation(
                                new FlightSeat("1A", SeatClass.FIRST, flight),
                                passenger
                        ))

                )
        );

        // WHEN
        BookingDto target = mapper.map(source);

        // THEN
        Assertions.assertEquals(source.getTotalCost(), target.getTotalCost());
        Assertions.assertEquals(source.getId().getValue().toString(), target.getId());
        Assertions.assertEquals(source.getDate(), target.getDateTime());
        Assertions.assertNotNull(target.getCustomer());
        Assertions.assertEquals(source.getCustomer().getUsername(), target.getCustomer().getUsername());
        Assertions.assertEquals(source.getCustomer().getEmail(), target.getCustomer().getEmail());
        Assertions.assertNotNull(target.getFlight());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getId().getValue().toString(), target.getFlight().getId());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getStatus(), target.getFlight().getStatus());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getArrival(), target.getFlight().getArrival());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getDeparture(), target.getFlight().getDeparture());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getCode(), target.getFlight().getCode());
        Assertions.assertNotNull(target.getFlight().getAirline());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getAirline().getName(), target.getFlight().getAirline().getName());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getAirline().getCode(), target.getFlight().getAirline().getCode());
        Assertions.assertNotNull(target.getFlight().getOrigin());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getOrigin().getLocation(), target.getFlight().getOrigin().getLocation());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getOrigin().getName(), target.getFlight().getOrigin().getName());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getOrigin().getCode(), target.getFlight().getOrigin().getCode());
        Assertions.assertNotNull(target.getFlight().getDestination());
        Assertions.assertNotNull(target.getFlight().getProfile());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getAirplaneProfile().getType(), target.getFlight().getProfile().getType());
        Assertions.assertEquals(source.getFlightBooking().getFlight().getAirplaneProfile().getCode(), target.getFlight().getProfile().getCode());
        Assertions.assertNotNull(target.getFlight().getSeatAllocations());
        Assertions.assertEquals(1, target.getFlight().getSeatAllocations().size());
        Assertions.assertNotNull(target.getFlight().getSeatAllocations().get(0));
        Assertions.assertEquals(source.getFlightBooking().getAllocations().iterator().next().getSeat().getName(), target.getFlight().getSeatAllocations().get(0).getSeat());
        Assertions.assertEquals(source.getFlightBooking().getAllocations().iterator().next().getSeat().getSeatClass(), target.getFlight().getSeatAllocations().get(0).getSeatClass());
        Assertions.assertEquals(source.getFlightBooking().getAllocations().iterator().next().getPassenger().getSurname(), target.getFlight().getSeatAllocations().get(0).getSurname());
        Assertions.assertEquals(source.getFlightBooking().getAllocations().iterator().next().getPassenger().getGivenName(), target.getFlight().getSeatAllocations().get(0).getGivenName());
        Assertions.assertNotNull(target.getReturnFlight());
    }
}
