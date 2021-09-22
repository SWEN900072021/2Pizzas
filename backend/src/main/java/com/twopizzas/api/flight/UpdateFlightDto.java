package com.twopizzas.api.flight;

import com.twopizzas.domain.flight.Flight;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class UpdateFlightDto {
    private Flight.Status status;
    private OffsetDateTime arrival;
    private OffsetDateTime departure;
    private ArrayList<NewFlightDto.StopOver> stopOvers;

    List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (arrival != null && departure != null && arrival.isBefore(departure)) {
            errors.add("arrival must occur after departure");
        }
        
        return errors;
    }
}
