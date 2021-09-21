package com.twopizzas.api.airport;

import lombok.Data;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewAirportDto {
    private String name;
    private String zoneId;
    private String code;
    private String location;

    List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            errors.add("name must not be blank");
        }

        if (zoneId == null || zoneId.trim().isEmpty()) {
            errors.add("name must not be blank");
        } else {
            try {
                ZoneId.of(zoneId);
            } catch (Exception e) {
                errors.add("zoneId must be a valid timezone id");
            }
        }

        if (code == null || code.trim().isEmpty()) {
            errors.add("name must not be blank");
        }

        return errors;
    }
}
