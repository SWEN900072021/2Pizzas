package com.twopizzas.api.flight;

import com.twopizzas.api.ValidationUtils;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.airport.AirportRepository;
import com.twopizzas.domain.booking.PassengerRepository;
import com.twopizzas.domain.flight.*;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.User;
import com.twopizzas.web.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FlightController {

    private static final FlightMapper MAPPER = Mappers.getMapper(FlightMapper.class);
    private final FlightRepository repository;;
    private final AirportRepository airportRepository;
    private final AirplaneProfileRepository airplaneProfileRepository;

    @Autowired
    public FlightController(FlightRepository repository, AirportRepository airportRepository, AirplaneProfileRepository airplaneProfileRepository) {
        this.repository = repository;
        this.airportRepository = airportRepository;
        this.airplaneProfileRepository = airplaneProfileRepository;
    }

    @RequestMapping(
            path = "/flight",
            method = HttpMethod.POST
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightDto> createFlight(@RequestBody NewFlightDto body, User user) throws HttpException {
        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        Airline airlineUser = (Airline) user;
        AirplaneProfile profile = airplaneProfileRepository.find(EntityId.of(body.getProfile()))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("airplane profile %s not found", body.getProfile())));
        Airport origin = airportRepository.find(EntityId.of(body.getOrigin()))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("airport %s not found", body.getOrigin())));
        Airport destination = airportRepository.find(EntityId.of(body.getDestination()))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("airport %s not found", body.getDestination())));
        List<StopOver> stopOvers = new ArrayList<>();
        for (NewFlightDto.StopOver stopOver: body.getStopOvers()) {
            stopOvers.add(new StopOver(
                    airportRepository.find(EntityId.of(stopOver.getLocation()))
                                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "airport %s not found")),
                    stopOver.getArrival(),
                    stopOver.getDeparture()
                ));
        }

        Flight newFlight = repository.save(new Flight(
                profile,
                airlineUser,
                origin,
                destination,
                stopOvers,
                body.getCode(),
                body.getDeparture(),
                body.getArrival(),
                body.getFirstClassCost(),
                body.getBusinessClassCost(),
                body.getEconomyClassCost()
        ));

        return RestResponse.ok(MAPPER.map(newFlight));
    }

    @RequestMapping(
            path = "/flight",
            method = HttpMethod.GET
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<List<FlightDto>> getAllFlights(User user) {
        return RestResponse.ok(repository.searchFlights(FlightSearch.builder().airline(user.getId()).build()).stream()
                .map(MAPPER::map)
                .collect(Collectors.toList()));
    }

    @RequestMapping(
            path = "/flight/{id}",
            method = HttpMethod.GET
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightDto> getFlightById(@PathVariable("id") String id, User user) throws HttpException {
        if (!ValidationUtils.isUUID(id)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "id must be a uuid");
        }

        Flight flight = repository.find(EntityId.of(id))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("flight %s not found", id)));

        if (!flight.getAirline().equals(user)) {
            throw new HttpException(HttpStatus.FORBIDDEN);
        }
        return RestResponse.ok(MAPPER.map(flight));
    }

    @RequestMapping(
            path = "/flight/{id}",
            method = HttpMethod.PATCH
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<FlightDto> updateFlight(@PathVariable("id") String id, @RequestBody UpdateFlightDto body, User user) throws HttpException {
        if (!ValidationUtils.isUUID(id)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "id must be a uuid");
        }

        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        Flight flight = repository.find(EntityId.of(id))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("flight %s not found", id)));

        if (!flight.getAirline().equals(user)) {
            throw new HttpException(HttpStatus.FORBIDDEN);
        }

        flight.setStatus(body.getStatus());
        repository.save(flight);

        return RestResponse.ok(MAPPER.map(flight));
    }

    @RequestMapping(
            path = "/flight/{id}/passenger",
            method = HttpMethod.GET
    )
    @Authenticated(Airline.TYPE)
    public RestResponse<List<FlightPassengerDto>> getFlightPassengers(@PathVariable("id") String id, User user) throws HttpException {
        if (!ValidationUtils.isUUID(id)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "id must be a uuid");
        }

        Flight flight = repository.find(EntityId.of(id))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("flight %s not found", id)));

        if (!flight.getAirline().equals(user)) {
            throw new HttpException(HttpStatus.FORBIDDEN);
        }

        return RestResponse.ok(flight.getAllocatedSeats().stream().map(MAPPER::map).collect(Collectors.toList()));
    }
}
