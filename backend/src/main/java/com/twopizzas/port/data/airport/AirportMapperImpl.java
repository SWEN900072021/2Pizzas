package com.twopizzas.port.data.airport;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
class AirportMapperImpl implements AirportMapper {

    static final String TABLE_AIRPORT = "airport";
    static final String COLUMN_ID = "id";
    static final String COLUMN_CODE = "code";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_LOCATION = "location";
    static final String COLUMN_UTC_OFFSET = "utcOffset";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_AIRPORT + "(" + COLUMN_ID + " , " + COLUMN_CODE + ", " + COLUMN_NAME + ", " + COLUMN_LOCATION + ", " + COLUMN_UTC_OFFSET + ")" +
            " VALUES (?, ?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_AIRPORT +
            " SET " + COLUMN_CODE + " = ?, " + COLUMN_NAME + " = ?, " + COLUMN_LOCATION + " = ?, " + COLUMN_UTC_OFFSET + " = ?" +
            " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_AIRPORT +
            " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_AIRPORT +
            " WHERE id = ?;";

    private final AirportTableResultSetMapper mapper = new AirportTableResultSetMapper();
    private final ConnectionPool connectionPool;

    @Autowired
    AirportMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(Airport entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getCode(),
                entity.getName(),
                entity.getLocation(),
                entity.getUtcOffset().getId()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public Airport read(EntityId entityId) {
        List<Airport> airports = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), mapper);
        if (airports.isEmpty()) {
            return null;
        }
        // maybe throw an error if there are more than one
        return airports.get(0);
    }

    @Override
    public List<Airport> readAll(AirportSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Airport entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getCode(),
                entity.getName(),
                entity.getLocation(),
                entity.getUtcOffset().normalized(),
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Airport entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }
}
