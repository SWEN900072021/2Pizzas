package com.twopizzas.api.airplaneProfile;

import com.twopizzas.domain.flight.AirplaneProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class AirplaneProfileMapperTests {

    private final AirplaneProfileMapper mapper = Mappers.getMapper(AirplaneProfileMapper.class);

    @Test
    @DisplayName("GIVEN valid AirplanProfile WHEN mapped THEN returns valid AirplaneProfileDto")
    void test() {
        // GIVEN
        AirplaneProfile source = new AirplaneProfile("name", "code", 1, 2, 3, 4, 5, 6);

        // WHEN
        AirplaneProfileDto target = mapper.map(source);

        // THEN
        Assertions.assertEquals(source.getId().getValue().toString(), target.getId());
        Assertions.assertEquals(source.getType(), target.getType());
        Assertions.assertEquals(source.getCode(), target.getCode());
        Assertions.assertEquals(source.getFirstClassRows(), target.getFirstClassRows());
        Assertions.assertEquals(source.getFirstClassColumns(), target.getFirstClassColumns());
        Assertions.assertEquals(source.getBusinessClassRows(), target.getBusinessClassRows());
        Assertions.assertEquals(source.getBusinessClassColumns(), target.getBusinessClassColumns());
        Assertions.assertEquals(source.getEconomyClassRows(), target.getEconomyClassRows());
        Assertions.assertEquals(source.getEconomyClassColumns(), target.getEconomyClassColumns());
    }
}
