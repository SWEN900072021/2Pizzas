package com.twopizzas.port.data.airline;

import com.twopizzas.domain.Airline;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AllAirlinesSpecification implements AirlineSpecification {

    private static final String TEMPLATE =
            "SELECT * FROM airline;";

    private final AirlineTableResultSetMapper mapper = new AirlineTableResultSetMapper();

    @Override
    public List<Airline> execute(ConnectionPool context) {
        return new SqlStatement(TEMPLATE).doQuery(context.getCurrentTransaction(), mapper);
    }
}
