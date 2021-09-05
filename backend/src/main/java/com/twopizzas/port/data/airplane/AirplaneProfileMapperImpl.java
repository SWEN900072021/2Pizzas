package com.twopizzas.port.data.airplane;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
class AirplaneProfileMapperImpl implements AirplaneProfileMapper {
    static final String TABLE_AIRPLANE = "airplaneProfile";
    static final String COLUMN_ID = "id";
    static final String COLUMN_CODE = "code";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_FIRST_CLASS_ROWS = "firstClassRows";
    static final String COLUMN_FIRST_CLASS_COLUMNS = "firstClassColumns";
    static final String COLUMN_BUSINESS_CLASS_ROWS = "businessClassRows";
    static final String COLUMN_BUSINESS_CLASS_COLUMNS = "businessClassColumns";
    static final String COLUMN_ECONOMY_CLASS_ROWS = "economyClassRows";
    static final String COLUMN_ECONOMY_CLASS_COLUMNS = "economyClassColumns";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_AIRPLANE + "(" + COLUMN_ID + ", " + COLUMN_CODE + ", " + COLUMN_TYPE +
                    ", " + COLUMN_FIRST_CLASS_ROWS + ", " + COLUMN_FIRST_CLASS_COLUMNS +
                    ", " + COLUMN_BUSINESS_CLASS_ROWS + ", " + COLUMN_BUSINESS_CLASS_COLUMNS +
                    ", " + COLUMN_ECONOMY_CLASS_ROWS + ", " + COLUMN_ECONOMY_CLASS_COLUMNS + ")" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_AIRPLANE +
                    " SET " + COLUMN_CODE + " = ?, " + COLUMN_TYPE + " = ?, "
                    + COLUMN_FIRST_CLASS_ROWS + " = ?, " + COLUMN_FIRST_CLASS_COLUMNS + " = ?, "
                    + COLUMN_BUSINESS_CLASS_ROWS + " = ?, " + COLUMN_BUSINESS_CLASS_COLUMNS + " = ?, "
                    + COLUMN_ECONOMY_CLASS_ROWS + " = ?, " + COLUMN_ECONOMY_CLASS_COLUMNS + " = ?" +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_AIRPLANE +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_AIRPLANE +
                    " WHERE id = ?;";

    private ConnectionPool connectionPool;

    @Autowired
    AirplaneProfileMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(AirplaneProfile entity) {
        new SqlStatement(INSERT_TEMPLATE,
                entity.getId().toString(),
                entity.getCode(),
                entity.getType(),
                entity.getFirstClassRows(),
                entity.getFirstClassColumns(),
                entity.getBusinessClassRows(),
                entity.getBusinessClassColumns(),
                entity.getEconomyClassRows(),
                entity.getEconomyClassColumns()).doExecute(connectionPool.getCurrentTransaction());
    }

    public AirplaneProfile read(EntityId entityId) {
        return new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), this)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<AirplaneProfile> readAll(AirplaneProfileSpecification  specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(AirplaneProfile entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getCode(),
                entity.getType(),
                entity.getFirstClassRows(),
                entity.getFirstClassColumns(),
                entity.getBusinessClassRows(),
                entity.getBusinessClassColumns(),
                entity.getEconomyClassRows(),
                entity.getEconomyClassColumns(),
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(AirplaneProfile entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    public List<AirplaneProfile> map(ResultSet resultSet) {
        List<AirplaneProfile> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                mapped.add(new AirplaneProfile(
                        EntityId.of(resultSet.getObject(AirplaneProfileMapperImpl.COLUMN_ID, String.class)),
                        resultSet.getObject(AirplaneProfileMapperImpl.COLUMN_CODE, String.class),
                        resultSet.getObject(AirplaneProfileMapperImpl.COLUMN_TYPE, String.class),
                        resultSet.getInt(AirplaneProfileMapperImpl.COLUMN_FIRST_CLASS_ROWS),
                        resultSet.getInt(AirplaneProfileMapperImpl.COLUMN_FIRST_CLASS_COLUMNS),
                        resultSet.getInt(AirplaneProfileMapperImpl.COLUMN_BUSINESS_CLASS_ROWS),
                        resultSet.getInt(AirplaneProfileMapperImpl.COLUMN_BUSINESS_CLASS_COLUMNS),
                        resultSet.getInt(AirplaneProfileMapperImpl.COLUMN_ECONOMY_CLASS_ROWS),
                        resultSet.getInt(AirplaneProfileMapperImpl.COLUMN_ECONOMY_CLASS_COLUMNS)
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", AirplaneProfile.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }
}