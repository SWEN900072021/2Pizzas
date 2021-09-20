package com.twopizzas.port.data.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.StopOver;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.airport.AirportMapper;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

class FlightStopOversLoader implements ValueLoader<List<StopOver>>, SqlResultSetMapper<StopOver> {

    static final String TABLE_STOPOVER = "stopover";
    static final String COLUMN_FLIGHT_ID = "flightId";
    static final String COLUMN_AIRPORT_ID = "airportId";
    static final String COLUMN_ARRIVAL = "arrival";
    static final String COLUMN_DEPARTURE = "departure";
    private static final String TEMPLATE =
            "SELECT * FROM " + TABLE_STOPOVER + " WHERE " + COLUMN_FLIGHT_ID + " = ?";

    private final ConnectionPool connectionPool;
    private final AirportMapper airportMapper;
    private final EntityId flightId;

    public FlightStopOversLoader(ConnectionPool connectionPool, AirportMapper airportMapper, EntityId flightId) {
        this.connectionPool = connectionPool;
        this.airportMapper = airportMapper;
        this.flightId = flightId;
    }

    @Override
    public ValueHolder<List<StopOver>> load() {
        return BaseValueHolder.of(map(new SqlStatement(TEMPLATE,
                flightId.toString())
                .doQuery(connectionPool.getCurrentTransaction())));
    }

    @Override
    public List<StopOver> map(ResultSet resultSet) {
        List<StopOver> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                mapped.add(new StopOver(
                        airportMapper.read(EntityId.of(resultSet.getObject(COLUMN_AIRPORT_ID, String.class))),
                        resultSet.getObject(COLUMN_ARRIVAL, OffsetDateTime.class).withOffsetSameInstant(ZoneOffset.UTC),
                        resultSet.getObject(COLUMN_DEPARTURE, OffsetDateTime.class).withOffsetSameInstant(ZoneOffset.UTC)
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Airport.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }

    @Override
    public StopOver mapOne(ResultSet resultSet) {
        return null;
    }
}
