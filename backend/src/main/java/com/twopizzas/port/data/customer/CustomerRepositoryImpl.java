package com.twopizzas.port.data.customer;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Customer;
import com.twopizzas.domain.CustomerRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
public class CustomerRepositoryImpl extends AbstractRepository<Customer, EntityId, CustomerSpecification, CustomerMapper> implements CustomerRepository {

    @Autowired
    public CustomerRepositoryImpl(CustomerMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return dataMapper.readAll(new AllCustomersSpecification());
    }
}