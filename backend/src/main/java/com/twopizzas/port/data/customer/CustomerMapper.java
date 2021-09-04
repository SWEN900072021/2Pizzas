package com.twopizzas.port.data.customer;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface CustomerMapper extends DataMapper<Customer, EntityId, CustomerSpecification> {
    @Override
    default Class<Customer> getEntityClass() {
        return Customer.class;
    }
}
