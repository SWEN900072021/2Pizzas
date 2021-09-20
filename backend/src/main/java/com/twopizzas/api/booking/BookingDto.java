package com.twopizzas.api.booking;

import com.twopizzas.domain.flight.SeatClass;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class BookingDto {
    private String id;
    private OffsetDateTime dateTime;
    private Flight flight;
    private Flight returnFlight;

    @Data
    public static class Flight {
        private String code;
        private OffsetDateTime departure;
        private OffsetDateTime arrival;
        private Airport origin;
        private Airport destination;
        private List<SeatAllocation> seatAllocations;
        private com.twopizzas.domain.flight.Flight.Status status;
        private Airline airline;
        private AirplaneProfile profile;
    }

    @Data
    public static class AirplaneProfile {
        private String name;
        private String code;
    }

    @Data
    private static class SeatAllocation {
        private String seat;
        private SeatClass seatClass;
        private String givenName;
        private String surname;
    }

    @Data
    public static class Airport {
        private String name;
        private String location;
        private String code;
    }

    @Data
    public static class Airline {
        private String name;
        private String code;
    }
}
