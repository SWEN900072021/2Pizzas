package com.twopizzas.port.data.airport;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;

public interface AirportMapper extends DataMapper<Airport, EntityId, AirportSpecification> {
    @Override
    default Class<Airport> getEntityClass() {
        return Airport.class;
    }
}
