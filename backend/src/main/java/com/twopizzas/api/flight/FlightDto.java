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
    private FlightSearchResultDto.Airport origin;
    private FlightSearchResultDto.Airport destination;
    private List<FlightSearchResultDto.Seat> seats;
    private List<FlightSearchResultDto.StopOver> stopovers;
    private Flight.Status status;
    private Airline airline;
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
        private OffsetDateTime arriving;
        private OffsetDateTime departing;
        private FlightSearchResultDto.Airport location;
    }

    @Data
    public static class Airline {
        private String name;
        private String code;
    }

    @Data
    public static class AirplaneProfile {
        private String name;
        private String code;
    }
}
