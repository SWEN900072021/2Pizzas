package com.twopizzas.api.flight;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.booking.PassengerRepository;
import com.twopizzas.domain.flight.FlightRepository;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.User;
import com.twopizzas.web.*;

@Controller
public class FlightController {

    private final FlightRepository repository;
    private final PassengerRepository passengerRepository;

    @Autowired
    public FlightController(FlightRepository repository, PassengerRepository passengerRepository) {
        this.repository = repository;
        this.passengerRepository = passengerRepository;
    }

    @RequestMapping(
            path = "/flight",
            method = HttpMethod.POST
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightDto> createFlight(@RequestBody NewFlightDto body, User user) {
        return null;
    }

    @RequestMapping(
            path = "/flight",
            method = HttpMethod.GET
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightDto> getAllFlights(@RequestBody NewFlightDto body, User user) {
        return null;
    }

    @RequestMapping(
            path = "/flight/{id}",
            method = HttpMethod.GET
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightDto> getFlightById(@PathVariable("id") String id, User user) {
        return null;
    }

    @RequestMapping(
            path = "/flight/{id}",
            method = HttpMethod.PATCH
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightDto> updateFlight(@PathVariable("id") String id, @RequestBody UpdateFlightDto body, User user) {
        return null;
    }

    @RequestMapping(
            path = "/flight/{id}/passenger",
            method = HttpMethod.GET
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightPassengerDto> getFlightPassengers(@PathVariable("id") String id, User user) {
        return null;
    }

}
