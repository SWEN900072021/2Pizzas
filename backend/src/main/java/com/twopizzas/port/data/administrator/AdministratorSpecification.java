package com.twopizzas.port.data.administrator;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.Administrator;
import com.twopizzas.port.data.db.ConnectionPool;

public interface AdministratorSpecification extends Specification<Administrator, ConnectionPool> {
}