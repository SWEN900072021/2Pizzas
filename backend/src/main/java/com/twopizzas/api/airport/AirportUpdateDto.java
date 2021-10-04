package com.twopizzas.api.airport;

import com.twopizzas.domain.airport.Airport;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AirportUpdateDto {
    private Airport.AirportStatus status;

    List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (status == null) {
            errors.add("status is required");
        }
        return errors;
    }
}
