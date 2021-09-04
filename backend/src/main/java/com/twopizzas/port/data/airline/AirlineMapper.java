package com.twopizzas.port.data.airline;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Airline;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface AirlineMapper extends DataMapper<Airline, EntityId, AirlineSpecification>, SqlResultSetMapper<Airline> {
    @Override
    default Class<Airline> getEntityClass() {
        return Airline.class;
    }
}
