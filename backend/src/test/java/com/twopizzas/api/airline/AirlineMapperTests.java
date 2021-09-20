package com.twopizzas.api.airline;

import com.twopizzas.domain.user.Airline;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class AirlineMapperTests {

    private AirlineMapper mapper = Mappers.getMapper(AirlineMapper.class);

    @Test
    @DisplayName("GIVEN valid Airline WHEN mapped THEN valid AirlineDto")
    void test() {
        // GIVEN
        Airline source = new Airline("someUsername", "somePassword", "someName", "someCode");

        // WHEN
        AirlineDto target = mapper.map(source);

        // THEN
        Assertions.assertEquals(source.getCode(), target.getCode());
        Assertions.assertEquals(source.getName(), target.getName());
        Assertions.assertEquals(source.getId().getValue().toString(), target.getId());
    }
}
