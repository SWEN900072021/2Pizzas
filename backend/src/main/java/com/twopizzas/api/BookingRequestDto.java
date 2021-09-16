package com.twopizzas.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
@Data
public class BookingRequestDto {
    private String flightId;
    private String returnId;
    private String customerId;
    private List<PassengerBooking> passengerBookings;

    @Data
    public static class PassengerBooking {
        private String givenName;
        private String lastName;
        private String passport;
        private LocalDate dateOfBirth;
        private String nationality;
        private List<SeatAllocationDto> seatAllocations;
    }

    @Data
    public static class SeatAllocationDto {
        private String flightId;
        private String seatName;
    }
}
