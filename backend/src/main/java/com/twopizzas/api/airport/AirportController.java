package com.twopizzas.api.airport;

import com.twopizzas.api.ValidationUtils;
import com.twopizzas.api.user.UserDto;
import com.twopizzas.api.user.UserUpdateDto;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.airport.AirportRepository;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.domain.user.User;
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
            path = "/airport/{id}",
            method = HttpMethod.PATCH
    )
    @Authenticated(Administrator.TYPE)
    public RestResponse<AirportDto> updateAirport(@RequestBody AirportUpdateDto body, @PathVariable("id") String id) throws HttpException {
        if (!ValidationUtils.isUUID(id)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "id must be a uuid");
        }

        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        Airport airport = repository.find(EntityId.of(id))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("airport %s not found", id)));

        airport.setStatus(body.getStatus());
        repository.save(airport);

        return RestResponse.ok(MAPPER.map(airport));
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
