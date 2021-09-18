package com.twopizzas.api.airplaneProfile;

import com.twopizzas.api.ValidationUtils;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.domain.flight.AirplaneProfileRepository;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.web.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AirplaneProfileController {

    private static final AirplaneProfileMapper MAPPER = Mappers.getMapper(AirplaneProfileMapper.class);
    private final AirplaneProfileRepository repository;

    @Autowired
    AirplaneProfileController(AirplaneProfileRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "/airplane-profile", method = HttpMethod.GET)
    public RestResponse<List<AirplaneProfileDto>> getAllAirplanes() {
        return RestResponse.ok(repository.findAllAirplanes().stream().map(MAPPER::map).collect(Collectors.toList()));
    }

    @RequestMapping(path = "/airplane-profile", method = HttpMethod.POST)
    @Authenticated(Administrator.TYPE)
    public RestResponse<AirplaneProfileDto> createAirplaneProfile(@RequestBody NewAirplaneProfileDto body) throws HttpException {
        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }
        AirplaneProfile newAirplaneProfile = repository.save(new AirplaneProfile(
                body.getCode(),
                body.getType(),
                body.getFirstClassRows(),
                body.getFirstClassColumns(),
                body.getBusinessClassRows(),
                body.getBusinessClassColumns(),
                body.getEconomyClassRows(),
                body.getEconomyClassColumns()
        ));
        return RestResponse.ok(MAPPER.map(newAirplaneProfile));
    }

    @RequestMapping(
            path = "/airplane-profile/{id}",
            method = HttpMethod.GET
    )
    public RestResponse<AirplaneProfileDto> getAirplaneProfileById(@PathVariable("id") String id) throws HttpException {
        if (!ValidationUtils.isUUID(id)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "id must be a uuid");
        }
        EntityId airplaneProfileId = EntityId.of(id);
        return repository.find(airplaneProfileId)
                .map(a -> RestResponse.ok(MAPPER.map(a)))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND));
    }
}
