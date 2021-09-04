package com.twopizzas.port.data.airline;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.Airline;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AirlineMapperImpl implements AirlineMapper {

    static final String TABLE_AIRLINE = "airline";
    static final String COLUMN_ID = "id";
    static final String COLUMN_CODE = "code";
    static final String COLUMN_NAME = "name";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_AIRLINE + "(" + COLUMN_ID + " , " + COLUMN_CODE + ", " + COLUMN_NAME + ")" +
                    " VALUES (?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_AIRLINE +
                    " SET " + COLUMN_CODE + " = ?, " + COLUMN_NAME + " = ?" +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_AIRLINE +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_AIRLINE +
                    " WHERE id = ?;";

    private final AirlineTableResultSetMapper mapper = new AirlineTableResultSetMapper();
    private final ConnectionPool connectionPool;

    @Autowired
    AirlineMapperImpl(ConnectionPool connectionPool) { this.connectionPool = connectionPool; }

    @Override
    public void create(Airline entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getCode().toString(),
                entity.getName().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public Airline read(EntityId entityId) {
        List<Airline> airlines = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), mapper);
        if (airlines.isEmpty()) {
            return null;
        }
        return airlines.get(0);
    }

    @Override
    public List<Airline> readAll(AirlineSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Airline entity) {
        new SqlStatement(UPDATE_TEMPLATE,
            entity.getCode(),
            entity.getName(),
            entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Airline entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }
}
