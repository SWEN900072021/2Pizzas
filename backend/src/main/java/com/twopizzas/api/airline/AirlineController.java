package com.twopizzas.api.airline;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
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
    private final AirlineRepository repository;

    @Autowired
    public AirlineController(AirlineRepository airlineRepository) {
        this.repository = airlineRepository;
    }

    @RequestMapping(
            path = "/airline",
            method = HttpMethod.GET
    )
    public RestResponse<List<AirlineDto>> getAllAirlines() {
        return RestResponse.ok(repository.findAllAirlines().stream().map(MAPPER::map).collect(Collectors.toList()));
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

        Airline newAirline = repository.save(new Airline(
                body.getUsername(),
                body.getPassword(),
                body.getName(),
                body.getCode()
        ));

        return RestResponse.ok(MAPPER.map(newAirline));
    }
}
