package com.twopizzas.api.flight;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlightPassengerDto {
    private String givenName;
    private String surname;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private String nationality;
    private String seatName;
    private String booking;
}
