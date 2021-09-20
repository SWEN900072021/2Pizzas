package com.twopizzas.api.flight;

import com.twopizzas.api.search.FlightSearchResultDto;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.SeatClass;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class FlightDto {
    private String id;
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    private Airport origin;
    private Airport destination;
    private List<Seat> seats;
    private List<StopOver> stopOvers;
    private Flight.Status status;
    private Airline airline;
    private String code;
    private AirplaneProfile profile;

    @Data
    public static class Seat {
        private String name;
        private SeatClass seatClass;
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
        private FlightSearchResultDto.Airport location;
    }

    @Data
    public static class Airline {
        private String name;
        private String code;
    }

    @Data
    public static class AirplaneProfile {
        private String type;
        private String code;
    }
}
