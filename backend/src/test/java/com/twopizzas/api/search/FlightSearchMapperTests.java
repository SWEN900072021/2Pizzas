package com.twopizzas.api.search;

import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.domain.flight.*;
import com.twopizzas.domain.user.Airline;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

public class FlightSearchMapperTests {

    private FlightSearchMapper mapper = Mappers.getMapper(FlightSearchMapper.class);

    @Test
    @DisplayName("GIVEN valid flight WHEN mapped THEN return valid FlightDto")
    void test() {
        // GIVEN
        OffsetDateTime now = OffsetDateTime.now();
        Flight source = Mockito.spy(new Flight(
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

        Passenger passenger = new Passenger("givenName", "surname", LocalDate.now(), "passport", "nationality");
        FlightSeat seat = source.getSeats().iterator().next();
        Mockito.when(source.getAllocatedSeats()).thenReturn(Collections.singletonList(
                new FlightSeatAllocation(seat, passenger)
        ));

        // WHEN
        FlightSearchResultDto target = mapper.map(source);

        // THEN
        Assertions.assertEquals(source.getId().getValue().toString(), target.getId());
        Assertions.assertEquals(source.getCode(), target.getCode());
        Assertions.assertEquals(source.getDeparture(), target.getDeparture());
        Assertions.assertEquals(source.getStatus(), target.getStatus());
        Assertions.assertEquals(source.getArrival(), target.getArrival());
        Assertions.assertNotNull(target.getAirline());
        Assertions.assertEquals(source.getAirline().getCode(), target.getAirline().getCode());
        Assertions.assertEquals(source.getAirline().getName(), target.getAirline().getName());
        Assertions.assertNotNull(target.getOrigin());
        Assertions.assertEquals(source.getOrigin().getCode(), target.getOrigin().getCode());
        Assertions.assertEquals(source.getOrigin().getName(), target.getOrigin().getName());
        Assertions.assertEquals(source.getOrigin().getLocation(), target.getOrigin().getLocation());
        Assertions.assertNotNull(target.getDestination());
        Assertions.assertNotNull(target.getProfile());
        Assertions.assertEquals(source.getAirplaneProfile().getCode(), target.getProfile().getCode());
        Assertions.assertEquals(source.getAirplaneProfile().getType(), target.getProfile().getType());
        Assertions.assertNotNull(target.getStopOvers());
        Assertions.assertEquals(1, target.getStopOvers().size());
        Assertions.assertNotNull(target.getStopOvers().get(0));
        Assertions.assertEquals(source.getStopOvers().get(0).getDeparture(), target.getStopOvers().get(0).getDeparture());
        Assertions.assertEquals(source.getStopOvers().get(0).getArrival(), target.getStopOvers().get(0).getArrival());
        Assertions.assertNotNull(target.getStopOvers().get(0).getLocation());
        Assertions.assertNotNull(target.getSeats());

        Optional<FlightSearchResultDto.Seat> targetSeat = target.getSeats().stream().filter(s -> seat.getName().equals(s.getName())).findFirst();
        Assertions.assertTrue(targetSeat.isPresent());
        Assertions.assertEquals(seat.getSeatClass(), targetSeat.get().getSeatClass());
        Assertions.assertEquals(seat.getName(), targetSeat.get().getName());
        Assertions.assertNotNull(targetSeat.get().getBooked());
        Assertions.assertTrue(targetSeat.get().getBooked());
    }
}
