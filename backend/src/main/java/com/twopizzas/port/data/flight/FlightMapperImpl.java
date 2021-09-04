package com.twopizzas.port.data.flight;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.util.List;

@Component
public class FlightMapperImpl implements FlightMapper {

    static final String TABLE_FLIGHT = "flight";
    static final String COLUMN_ID = "id";
    static final String COLUMN_CODE = "code";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_LOCATION = "location";
    static final String COLUMN_UTC_OFFSET = "utcOffset";

    private static final String create =
            "INSERT INTO " + TABLE_FLIGHT + "(" + COLUMN_ID + " , " + COLUMN_CODE + ", " + COLUMN_NAME + ", " + COLUMN_LOCATION + ", " + COLUMN_UTC_OFFSET + ")" +
                    " VALUES (?, ?, ?, ?, ?);";

    private static final String update =
            "UPDATE " + TABLE_FLIGHT +
                    " SET " + COLUMN_CODE + " = ?, " + COLUMN_NAME + " = ?, " + COLUMN_LOCATION + " = ?, " + COLUMN_UTC_OFFSET + " = ?" +
                    " WHERE id = ?;";

    private static final String delete =
            "DELETE FROM " + TABLE_FLIGHT +
                    " WHERE id = ?;";

    private static final String select =
            "SELECT * FROM " + TABLE_FLIGHT +
                    " WHERE id = ?;";

    private final ConnectionPool connectionPool;

    @Autowired
    protected FlightMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(Flight entity) {

    }

    @Override
    public Flight read(EntityId entityId) {
        return null;
    }

    @Override
    public List<Flight> readAll(FlightSpecification specification) {
        return null;
    }

    @Override
    public void update(Flight entity) {

    }

    @Override
    public void delete(Flight entity) {

    }

    @Override
    public List<Flight> map(ResultSet resultSet) {
        return null;
    }
}
