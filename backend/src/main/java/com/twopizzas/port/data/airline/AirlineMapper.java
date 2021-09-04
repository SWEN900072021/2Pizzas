package com.twopizzas.port.data.airline;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.Airline;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.user.UserMapper;

public interface AirlineMapper extends DataMapper<Airline, EntityId, AirlineSpecification> {
}
