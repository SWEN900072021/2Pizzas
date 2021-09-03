package com.twopizzas.port.data.airport;

import com.twopizzas.domain.Airport;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AllAirportsSpecification implements AirportSpecification {

    private static final String TEMPLATE =
            "SELECT * FROM airport;";

    private final AirportTableResultSetMapper mapper = new AirportTableResultSetMapper();

    @Override
    public List<Airport> execute(ConnectionPool context) {
        return new SqlStatement(TEMPLATE).doQuery(context.getCurrentTransaction(), mapper);
    }
}