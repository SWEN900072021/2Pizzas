package com.twopizzas.port.data.airplane;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.db.ConnectionPool;

public interface AirplaneProfileSpecification extends Specification<AirplaneProfile, ConnectionPool> {
}
