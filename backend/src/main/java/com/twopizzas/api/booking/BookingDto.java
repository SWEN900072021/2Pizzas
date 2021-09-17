package com.twopizzas.api.booking;

import com.twopizzas.domain.flight.SeatClass;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class BookingDto {
    private String id;
    private OffsetDateTime dateTime;
    private Flight flight;
    private Flight returnFlight;

    public static class Flight {
        private String code;
        private OffsetDateTime departure;
        private OffsetDateTime arrival;
        private Airport origin;
        private Airport destination;
        private List<SeatAllocation> seats;
    }

    private static class SeatAllocation {
        private String seat;
        private SeatClass seatClass;
        private String givenName;
        private String surname;
    }

    public static class Airport {
        private String name;
        private String location;
        private String code;
    }
}
