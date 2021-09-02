package com.twopizzas.port.data.flight;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Flight;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface FlightMapper extends DataMapper<Flight, EntityId, FlightSpecification>, SqlResultSetMapper<Flight> {
    @Override
    default Class<Flight> getEntityClass() {
        return Flight.class;
    }
}
