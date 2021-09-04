package com.twopizzas.port.data.seat;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface FlightSeatMapper extends DataMapper<FlightSeat, EntityId, FlightSeatSpecification>, SqlResultSetMapper<FlightSeat> {
    @Override
    default Class<FlightSeat> getEntityClass() {
        return FlightSeat.class;
    }
}
