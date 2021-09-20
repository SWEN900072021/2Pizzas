package com.twopizzas.api.airport;

import lombok.Data;

@Data
public class AirportDto {
    private String name;
    private String id;
    private String code;
    private String location;
    private String zoneId;
}
