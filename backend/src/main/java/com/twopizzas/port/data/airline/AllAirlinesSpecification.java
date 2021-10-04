package com.twopizzas.port.data.airline;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AllAirlinesSpecification implements AirlineSpecification {

    private static final String TEMPLATE =
            "SELECT * FROM \"user\"" +
            " LEFT JOIN airline ON \"user\".id = airline.id" +
            " WHERE userType = '" + Airline.TYPE + "';";

    private final AirlineMapper mapper;

    @Autowired
    AllAirlinesSpecification(AirlineMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Airline> execute(ConnectionPool context) {
        return new SqlStatement(TEMPLATE).doQuery(context.getCurrentTransaction(), mapper);
    }
}
