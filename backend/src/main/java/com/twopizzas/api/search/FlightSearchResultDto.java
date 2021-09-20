package com.twopizzas.api.search;

import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.SeatClass;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class FlightSearchResultDto {
    private String id;
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    private Airport origin;
    private Airport destination;
    private List<Seat> seats;
    private List<StopOver> stopOvers;
    private Airline airline;
    private String code;
    private Flight.Status status;
    private AirplaneProfile profile;

    @Data
    public static class AirplaneProfile {
        private String type;
        private String code;
    }

    @Data
    public static class Seat {
        private String name;
        private SeatClass seatClass;
        private Boolean booked;
    }

    @Data
    public static class Airport {
        private String name;
        private String code;
        private String location;
    }

    @Data
    public static class StopOver {
        private OffsetDateTime arrival;
        private OffsetDateTime departure;
        private Airport location;
    }

    @Data
    public static class Airline {
        private String name;
        private String code;
    }
}
