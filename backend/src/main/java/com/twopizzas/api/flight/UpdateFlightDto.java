package com.twopizzas.api.flight;

import com.twopizzas.domain.flight.Flight;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateFlightDto {
    private Flight.Status status;

    List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (status == null) {
            errors.add("status is required");
        }

        return errors;
    }
}
