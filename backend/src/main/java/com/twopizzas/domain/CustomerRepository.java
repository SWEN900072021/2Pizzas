package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.List;

public interface CustomerRepository extends Repository<Customer, EntityId> {
    List<Customer> findAllCustomers();
}
