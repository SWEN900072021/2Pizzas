package com.twopizzas.port.data.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.*;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightSeatsLoader implements ValueLoader<List<FlightSeat>> {

    private static final String TEMPLATE = "SELECT * FROM seat WHERE flightId = ?;";

    private final EntityId flightId;
    private final ConnectionPool connection;

    public FlightSeatsLoader(EntityId flightId, ConnectionPool connection) {
        this.flightId = flightId;
        this.connection = connection;
    }

    @Override
    public ValueHolder<List<FlightSeat>> load() {
        ResultSet resultSet = new SqlStatement(TEMPLATE, flightId.toString()).doQuery(connection.getCurrentTransaction());
        return BaseValueHolder.of(mapResults(resultSet));
    }

    private List<FlightSeat> mapResults(ResultSet resultSet) {

        List<FlightSeat> mapped = new ArrayList<>();

        try {
            while (resultSet.next()) {

            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", FlightSeat.class.getName(), e.getMessage()),
                    e);
        }

        return mapped;
    }
}
