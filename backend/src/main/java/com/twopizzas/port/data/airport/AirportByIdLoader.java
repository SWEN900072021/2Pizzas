package com.twopizzas.port.data.airport;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;

public class AirportByIdLoader implements ValueLoader<Airport> {

    private final AirportMapper mapper;
    private final EntityId airportId;


    public AirportByIdLoader(AirportMapper mapper, EntityId airportId) {
        this.mapper = mapper;
        this.airportId = airportId;
    }
    
    @Override
    public ValueHolder<Airport> load() {
        return BaseValueHolder.of(mapper.read(airportId));
    }
}
