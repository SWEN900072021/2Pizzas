package com.twopizzas.api.airport;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.airport.AirportRepository;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.web.*;
import org.mapstruct.factory.Mappers;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AirportController {

    private static final AirportMapper MAPPER = Mappers.getMapper(AirportMapper.class);
    private final AirportRepository repository;

    @Autowired
    AirportController(AirportRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(
            path = "/airport",
            method = HttpMethod.GET
    )
    public RestResponse<List<AirportDto>> getAllAirports() {
        return RestResponse.ok(repository.findAllAirports().stream().map(MAPPER::map).collect(Collectors.toList()));
    }

    @RequestMapping(
            path = "/airport",
            method = HttpMethod.POST
    )
    @Authenticated(Administrator.TYPE)
    public RestResponse<AirportDto> createAirport(@RequestBody NewAirportDto body) throws HttpException {
        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }
        Airport newAirport = repository.save(new Airport(
                body.getCode(),
                body.getName(),
                body.getLocation(),
                ZoneId.of(body.getZoneId())
        ));
        return RestResponse.ok(MAPPER.map(newAirport));
    }
}
