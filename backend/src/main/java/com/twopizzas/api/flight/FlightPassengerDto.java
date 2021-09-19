package com.twopizzas.api.flight;

import lombok.Data;

@Data
public class FlightPassengerDto {
    private String givenName;
    private String surname;
    private String passportNumber;
    private String dateOfBirth;
    private String nationality;
    private String seatName;
    private String booking;
}
