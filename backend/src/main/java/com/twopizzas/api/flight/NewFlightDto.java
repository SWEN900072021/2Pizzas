package com.twopizzas.api.flight;

import com.twopizzas.api.ValidationUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewFlightDto {
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    private String origin;
    private String destination;
    private List<StopOver> stopOvers;
    private String code;
    private String profile;
    private final BigDecimal firstClassCost;
    private final BigDecimal businessClassCost;
    private final BigDecimal economyClassCost;

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (departure == null) {
            errors.add("departure is required");
        }

        if (arrival == null) {
            errors.add("arrival is required");
        }

        if (departure != null && arrival != null && departure.isAfter(arrival)) {
            errors.add("arrival must occur after departure");
        }

        if (!ValidationUtils.isUUID(origin)) {
            errors.add("origin must be a uuid");
        }

        if (!ValidationUtils.isUUID(destination)) {
            errors.add("destination must be a uuid");
        }

        if (code == null || code.trim().isEmpty()) {
            errors.add("code must not be blank");
        }

        if (!ValidationUtils.isUUID(profile)) {
            errors.add("profile must be a uuid");
        }

        if (firstClassCost == null || firstClassCost.signum() == -1) {
            errors.add("firstClassCost must not be less than 0");
        }

        if (businessClassCost == null || businessClassCost.signum() == -1) {
            errors.add("businessClassCost must not be less than 0");
        }

        if (economyClassCost == null || economyClassCost.signum() == -1) {
            errors.add("economyClassCost must not be less than 0");
        }

        if (stopOvers != null ) {
            for (int i = 0; i < stopOvers.size(); i++) {
                StopOver stopOver = stopOvers.get(i);
                String path = String.format("stopOvers[%s].", i);
                if (stopOver.getDeparture() == null) {
                    errors.add(path + "departure is required");
                }

                if (stopOver.getArrival() == null) {
                    errors.add(path + "arrival is required");
                }

                if (stopOver.getDeparture() != null && stopOver.getArrival() != null && stopOver.getDeparture().isAfter(stopOver.getArrival())) {
                    errors.add(path + "arrival must occur after " + path + "departure");
                }

                if (!ValidationUtils.isUUID(stopOver.getLocation())) {
                    errors.add(path + "location must be a uuid");
                }
            }
        }

        return errors;
    }

    @Data
    public static class StopOver {
        private OffsetDateTime departure;
        private OffsetDateTime arrival;
        private String location;
    }
}
