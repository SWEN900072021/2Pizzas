package com.twopizzas.port.data.passenger;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface PassengerMapper extends DataMapper<Passenger, EntityId, PassengerSpecification>, SqlResultSetMapper<Passenger> {

    @Override
    default Class<Passenger> getEntityClass() {
        return Passenger.class;
    }


}
