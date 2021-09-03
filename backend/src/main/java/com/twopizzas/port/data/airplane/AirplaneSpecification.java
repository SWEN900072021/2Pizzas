package com.twopizzas.port.data.airplane;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.AirplaneProfile;
import com.twopizzas.port.data.db.ConnectionPool;

public interface AirplaneSpecification extends Specification<Airplane, ConnectionPool> {
}
