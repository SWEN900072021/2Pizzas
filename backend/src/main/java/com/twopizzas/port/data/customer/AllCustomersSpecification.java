package com.twopizzas.port.data.customer;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AllCustomersSpecification implements CustomerSpecification {

    private static final String TEMPLATE =
            "SELECT * FROM customer;";

    private final CustomerMapper mapper;

    @Autowired
    AllCustomersSpecification(CustomerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Customer> execute(ConnectionPool context) {
        return new SqlStatement(TEMPLATE).doQuery(context.getCurrentTransaction(), mapper);
    }
}
