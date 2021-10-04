package com.twopizzas.api.booking;

import com.twopizzas.api.search.FlightSearchResultDto;
import com.twopizzas.domain.flight.SeatClass;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class BookingDto {
    private String id;
    private OffsetDateTime dateTime;
    private Flight flight;
    private Flight returnFlight;
    private BigDecimal totalCost;
    private Customer customer;

    @Data
    public static class Flight {
        private String id;
        private String code;
        private OffsetDateTime departure;
        private OffsetDateTime arrival;
        private OffsetDateTime departureLocal;
        private OffsetDateTime arrivalLocal;
        private Airport origin;
        private Airport destination;
        private List<SeatAllocation> seatAllocations;
        private List<StopOver> stopOvers;
        private com.twopizzas.domain.flight.Flight.FlightStatus status;
        private Airline airline;
        private AirplaneProfile profile;
    }

    @Data
    public static class StopOver {
        private OffsetDateTime arrival;
        private OffsetDateTime departure;
        private FlightSearchResultDto.Airport location;
        private OffsetDateTime departureLocal;
        private OffsetDateTime arrivalLocal;
    }

    @Data
    public static class Customer {
        String username;
        String email;
    }

    @Data
    public static class AirplaneProfile {
        private String code;
        private String type;
    }

    @Data
    public static class SeatAllocation {
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
