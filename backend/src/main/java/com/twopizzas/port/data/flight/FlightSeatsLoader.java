package com.twopizzas.port.data.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.LazyValueHolderProxy;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Seat;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.db.ConnectionPoolImpl;

import java.sql.ResultSet;
import java.util.List;

public class FlightSeatsLoader implements LazyValueHolderProxy.ValueLoader<List<Seat>> {

    private static final String TEMPLATE = "";

    private final EntityId flightId;
    private final ConnectionPool connection;

    public FlightSeatsLoader(EntityId flightId, ConnectionPool connection) {
        this.flightId = flightId;
        this.connection = connection;
    }

    @Override
    public ValueHolder<List<Seat>> load() {
        ResultSet resultSet = new SqlStatement(TEMPLATE, flightId.toString()).doQuery(connection.getCurrentTransaction());
        return BaseValueHolder.of(mapResults(resultSet));
    }

    private List<Seat> mapResults(ResultSet resultSet) {
        return null;
    }
}
