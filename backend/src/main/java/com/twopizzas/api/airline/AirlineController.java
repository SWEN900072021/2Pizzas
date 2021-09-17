package com.twopizzas.api.airline;

import com.twopizzas.api.ValidationUtils;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.AirlineRepository;
import com.twopizzas.web.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AirlineController {

    private static final AirlineMapper MAPPER = Mappers.getMapper(AirlineMapper.class);
    private final AirlineRepository airlineRepository;

    public AirlineController(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @RequestMapping(
            path = "/airline",
            method = HttpMethod.GET
    )
    @Authenticated(Administrator.TYPE)
    public RestResponse<List<AirlineDto>> getAllAirlines() {
        return RestResponse.ok(airlineRepository.findAllAirlines().stream().map(MAPPER::map).collect(Collectors.toList()));
    }

    @RequestMapping(
            path = "/airline",
            method = HttpMethod.POST
    )
    @Authenticated(Administrator.TYPE)
    public RestResponse<AirlineDto> createAirline(@RequestBody  NewAirlineDto body) throws HttpException {
        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        Airline newAirline = airlineRepository.save(new Airline(
                body.getUsername(),
                body.getPassword(),
                body.getName(),
                body.getCode()
        ));

        return RestResponse.ok(MAPPER.map(newAirline));
    }

    @RequestMapping(
            path = "/airline/{id}",
            method = HttpMethod.GET
    )
    @Authenticated(Administrator.TYPE)
    public RestResponse<AirlineDto> getAirlineById(@PathVariable("id") String id) throws HttpException {
        if (!ValidationUtils.isUUID(id)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "id must be a uuid");
        }
        EntityId airlineId = EntityId.of(id);
        return airlineRepository.find(airlineId)
                .map(a -> RestResponse.ok(MAPPER.map(a)))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND));
    }
}
