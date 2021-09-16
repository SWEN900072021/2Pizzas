package com.twopizzas.port.data.customer;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.port.data.db.ConnectionPool;

public interface CustomerSpecification extends Specification<Customer, ConnectionPool> {
}
