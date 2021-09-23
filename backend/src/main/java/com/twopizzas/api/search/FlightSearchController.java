package com.twopizzas.api.search;

import com.twopizzas.api.ValidationUtils;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.booking.TimePeriod;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightRepository;
import com.twopizzas.domain.flight.FlightSearch;
import com.twopizzas.web.*;
import org.mapstruct.factory.Mappers;

import javax.xml.crypto.Data;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FlightSearchController {

    private static final FlightSearchMapper MAPPER = Mappers.getMapper(FlightSearchMapper.class);
    private final FlightRepository repository;

    @Autowired
    public FlightSearchController(FlightRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(
            path = "/search/flight",
            method = HttpMethod.GET
    )
    public RestResponse<List<FlightSearchResultDto>> searchFlights(
        @QueryParameter("destination") String destination,
        @QueryParameter("origin") String origin,
        @QueryParameter("departingAfter") String departingAfter,
        @QueryParameter("departingBefore") String departingBefore,
        @QueryParameter("airline") String airline) throws HttpException {
        List<String> errors = new ArrayList<>();
        FlightSearch.FlightSearchBuilder builder = FlightSearch.builder();

        OffsetDateTime departingAfterOffset = null;
        OffsetDateTime departingBeforeOffset = null;
        if (departingAfter != null) {
            if (!ValidationUtils.isOffsetDateTime(departingAfter)) {
                errors.add("departingAfter must be a valid date-time");
            } else {
                departingAfterOffset = OffsetDateTime.parse(departingAfter);
            }
        }

        if (departingBefore != null) {
            if (!ValidationUtils.isOffsetDateTime(departingBefore)) {
                errors.add("departingBefore must be a valid date-time");
            } else {
                departingBeforeOffset = OffsetDateTime.parse(departingBefore);
            }
        }

        if (departingAfterOffset != null && departingBeforeOffset != null) {
            if (departingAfterOffset.isBefore(departingBeforeOffset)) {
                errors.add("departingAfter must not be before departingBefore");
            } else {
                builder.departing(new TimePeriod(departingAfterOffset, departingBeforeOffset));
            }
        }

        if (departingAfterOffset != null && departingBeforeOffset == null) {
            errors.add("departingBefore is required when departingAfter is provided");
        }

        if (departingAfterOffset == null && departingBeforeOffset != null) {
            errors.add("departingAfter is required when departingBefore is provided");
        }

        if (origin != null) {
            if (!ValidationUtils.isUUID(origin)) {
                errors.add("origin must be a uuid");
            } else {
                builder.from(EntityId.of(origin));
            }
        }

        if (destination != null) {
            if (!ValidationUtils.isUUID(destination)) {
                errors.add("destination must be a uuid");
            } else {
                builder.to(EntityId.of(destination));
            }
        }

        if (airline != null) {
            if (!ValidationUtils.isUUID(airline)) {
                errors.add("airline must be a uuid");
            } else {
                builder.airline(EntityId.of(airline));
            }
        }

        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        return RestResponse.ok(repository.searchFlights(builder.build()).stream()
                .filter(f -> !f.getStatus().equals(Flight.Status.CANCELLED))
                .filter(f -> f.getDeparture().isAfter(OffsetDateTime.now()))
                .map(MAPPER::map)
                .collect(Collectors.toList()));
    }
}
