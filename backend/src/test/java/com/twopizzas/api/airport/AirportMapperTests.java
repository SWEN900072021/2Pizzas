package com.twopizzas.api.airport;

import com.twopizzas.domain.airport.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.ZoneId;

public class AirportMapperTests {

    private AirportMapper mapper = Mappers.getMapper(AirportMapper.class);

    @Test
    @DisplayName("GIVEN valid Airport WHEN mapped THEN returns valid AirportDto")
    void test() {
        // GIVEN
        Airport source = new Airport("COD", "name", "location", ZoneId.of("UTC+01:00"));

        // WHEN
        AirportDto target = mapper.map(source);

        // THEN
        Assertions.assertEquals(source.getId().getValue().toString(), target.getId());
        Assertions.assertEquals(source.getCode(), target.getCode());
        Assertions.assertEquals(source.getLocation(), target.getLocation());
        Assertions.assertEquals(source.getName(), target.getName());
        Assertions.assertEquals(source.getUtcOffset().toString(), target.getZoneId());
    }
}
