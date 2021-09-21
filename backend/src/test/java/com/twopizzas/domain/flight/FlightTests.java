package com.twopizzas.domain.flight;

import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.user.Airline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class FlightTests {

    @Test
    @DisplayName("GIVEN flight has vacant seats WHEN allocate vacant seat THEN returns allocations")
    void test() {
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

        
    }

    @Test
    @DisplayName("GIVEN flight has no vacant seats WHEN allocate booked seat THEN throws business error")
    void test2() {

    }
}
