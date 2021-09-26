package com.twopizzas.api.flight;

import com.twopizzas.api.ValidationUtils;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.airport.AirportRepository;
import com.twopizzas.domain.flight.*;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.User;
import com.twopizzas.web.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FlightController {

    private static final FlightMapper MAPPER = Mappers.getMapper(FlightMapper.class);
    private final FlightRepository repository;
    private final AirportRepository airportRepository;
    private final AirplaneProfileRepository airplaneProfileRepository;
    private final FlightSeatRepository flightSeatRepository;

    @Autowired
    public FlightController(FlightRepository repository, AirportRepository airportRepository, AirplaneProfileRepository airplaneProfileRepository, FlightSeatRepository flightSeatRepository) {
        this.repository = repository;
        this.airportRepository = airportRepository;
        this.airplaneProfileRepository = airplaneProfileRepository;
        this.flightSeatRepository = flightSeatRepository;
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

        assertAirport(origin);
        assertAirport(destination);

        List<StopOver> stopOvers = new ArrayList<>();
        if (body.getStopOvers() != null) {
            for (NewFlightDto.StopOver stopOver : body.getStopOvers()) {
                stopOvers.add(new StopOver(
                        assertAirport(airportRepository.find(EntityId.of(stopOver.getLocation()))
                                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "airport %s not found"))),
                        stopOver.getArrival(),
                        stopOver.getDeparture()
                ));
            }
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

        newFlight.getSeats().forEach(flightSeatRepository::save);

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

        if (body.getStatus() != null) {
            flight.setStatus(body.getStatus());
        }

        if (body.getArrival() != null) {
            flight.setArrival(body.getArrival());
        }

        if (body.getDeparture() != null) {
            flight.setDeparture(body.getDeparture());
        }

        if (flight.getDeparture().isAfter(flight.getArrival())) {
            throw new HttpException(HttpStatus.CONFLICT, "arrival must occur after departure");
        }

        if (body.getStopOvers() != null) {
            List<StopOver> stopOvers = new ArrayList<>();
            for (NewFlightDto.StopOver stopOver : body.getStopOvers()) {
                stopOvers.add(new StopOver(
                        assertAirport(airportRepository.find(EntityId.of(stopOver.getLocation()))
                                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "airport %s not found"))),
                        stopOver.getArrival(),
                        stopOver.getDeparture()
                ));
            }
            flight.setStopOvers(stopOvers);
        }

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

    private Airport assertAirport(Airport airport) throws HttpException {
        if (airport.getStatus().equals(Airport.AirportStatus.INACTIVE)) {
            throw new HttpException(HttpStatus.FORBIDDEN, String.format("airport %s is in %s state", airport.getId(), airport.getStatus()));
        }
        return airport;
    }

    private StopOver assertStopover(StopOver stopOver, Flight flight) throws HttpException {
        if (flight.getDeparture().isAfter(stopOver.getDeparture()) || flight.getArrival().isBefore(stopOver.getDeparture()) ||
                flight.getDeparture().isAfter(stopOver.getArrival()) || flight.getArrival().isBefore(stopOver.getArrival())) {
            throw new HttpException(HttpStatus.CONFLICT, "invalid stopover for flight");
        }
        return stopOver;
    }
}
