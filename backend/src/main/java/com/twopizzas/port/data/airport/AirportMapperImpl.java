package com.twopizzas.port.data.airport;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.AbstractSqlDataMapper;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.db.SqlConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
class AirportMapperImpl extends AbstractSqlDataMapper<Airport, EntityId, AirportSpecification> implements AirportMapper {

    private static final String TABLE_AIRPORT = "airport";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_UTC_OFFSET = "utcOffset";

    private static final String create =
            "INSERT INTO " + TABLE_AIRPORT + "(" + COLUMN_ID + " , " + COLUMN_CODE + ", " + COLUMN_NAME + ", " + COLUMN_LOCATION + ", " + COLUMN_UTC_OFFSET + ")" +
            " VALUES (?, ?, ?, ?, ?);";

    private static final String update =
            "UPDATE " + TABLE_AIRPORT +
            " SET " + COLUMN_CODE + " = ?, " + COLUMN_NAME + " = ?, " + COLUMN_LOCATION + " = ?, " + COLUMN_UTC_OFFSET + " = ?" +
            " WHERE id = ?;";

    private static final String delete =
            "DELETE FROM " + TABLE_AIRPORT +
            " WHERE id = ?;";

    private static final String select =
            "SELECT * FROM " + TABLE_AIRPORT +
            " WHERE id = ?;";

    @Autowired
    AirportMapperImpl(SqlConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    public void create(Airport entity) {
        doExecute(create,
                entity.getId().toString(),
                entity.getCode(),
                entity.getName(),
                entity.getLocation(),
                entity.getUtcOffset().getId()
        );
    }

    @Override
    public Airport read(EntityId entityId) {
        ResultSet results = doQuery(select, entityId.toString());
        List<Airport> airports = mapResultSet(results);
        if (airports.isEmpty()) {
            return null;
        }
        // maybe throw an error if there are more than one
        return airports.get(0);
    }

    @Override
    public List<Airport> readAll(AirportSpecification specification) {
        return null;
    }

    @Override
    public void update(Airport entity) {
        doExecute(update,
                entity.getCode(),
                entity.getName(),
                entity.getLocation(),
                entity.getUtcOffset().normalized(),
                entity.getId().toString()
        );
    }

    @Override
    public void delete(Airport entity) {
        doExecute(delete,
                entity.getId().toString()
        );
    }

    public List<Airport> mapResultSet(ResultSet resultSet) {
        List<Airport> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                mapped.add(new Airport(
                        EntityId.of(resultSet.getObject(COLUMN_ID, String.class)),
                        resultSet.getObject(COLUMN_CODE, String.class),
                        resultSet.getObject(COLUMN_NAME, String.class),
                        resultSet.getObject(COLUMN_LOCATION, String.class),
                        ZoneId.of(resultSet.getObject(COLUMN_UTC_OFFSET, String.class))
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from read query to %s entity, error: %s", getEntityClass().getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }
}
