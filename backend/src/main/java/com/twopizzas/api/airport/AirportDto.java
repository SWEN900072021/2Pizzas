package com.twopizzas.api.airport;

import com.twopizzas.domain.airport.Airport;
import lombok.Data;

@Data
public class AirportDto {
    private String name;
    private String id;
    private String code;
    private String location;
    private String zoneId;
    private Airport.Status status;
}
