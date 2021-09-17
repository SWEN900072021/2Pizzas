package com.twopizzas.api.search;

import com.twopizzas.api.ValidationUtils;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.booking.TimePeriod;
import com.twopizzas.domain.flight.FlightSearch;
import com.twopizzas.web.HttpMethod;
import com.twopizzas.web.QueryParameter;
import com.twopizzas.web.RequestMapping;
import com.twopizzas.web.RestResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FlightSearchController {

    @RequestMapping(
            path = "/search/flight",
            method = HttpMethod.GET
    )
    public RestResponse<List<FlightSearchResultDto>> searchFlights(
        @QueryParameter("destination") String destination,
        @QueryParameter("origin") String origin,
        @QueryParameter("departingAfter") String departingAfter,
        @QueryParameter("departingBefore") String departingBefore,
        @QueryParameter("airline") String airline)
    {
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
    }
}
