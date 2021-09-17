package com.twopizzas.api.airport;

import lombok.Data;

import java.time.ZoneId;

@Data
public class AirportDto {
    private String code;
    private String name;
    private String location;
    private ZoneId utcOffset;
}
