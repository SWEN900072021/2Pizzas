package com.twopizzas.port.data.customer;

import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.CustomerRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
class CustomerRepositoryImpl extends AbstractRepository<Customer, CustomerSpecification, CustomerMapper> implements CustomerRepository {

    @Autowired
    public CustomerRepositoryImpl(CustomerMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return dataMapper.readAll(new AllCustomersSpecification(dataMapper));
    }
}
