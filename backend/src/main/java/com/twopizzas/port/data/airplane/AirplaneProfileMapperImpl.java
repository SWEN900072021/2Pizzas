package com.twopizzas.port.data.airplane;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.util.List;

@Component
class AirplaneProfileMapperImpl implements AirplaneProfileMapper {
    static final String TABLE_AIRPLANE = "airplane";
    static final String COLUMN_ID = "id";
    static final String COLUMN_CODE = "code";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_FIRSTCLASSROWS = "firstclassrows";
    static final String COLUMN_FIRSTCLASSCOLUMNS = "firstclasscolumns";
    static final String COLUMN_BUSINESSCLASSROWS = "businessclassrows";
    static final String COLUMN_BUSINESSCLASSCOLUMNS = "businessclasscolumns";
    static final String COLUMN_ECONOMYCLASSROWS = "economyclassrows";
    static final String COLUMN_ECONOMYCLASSCOLUMNS = "economyclasscolumns";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_AIRPLANE + "(" + COLUMN_ID + ", " + COLUMN_CODE + ", " + COLUMN_TYPE +
                    ", " + COLUMN_FIRSTCLASSROWS + ", " + COLUMN_FIRSTCLASSCOLUMNS +
                    ", " + COLUMN_BUSINESSCLASSROWS + ", " + COLUMN_BUSINESSCLASSCOLUMNS +
                    ", " + COLUMN_ECONOMYCLASSROWS + ", " + COLUMN_ECONOMYCLASSCOLUMNS + ")" + " VALUES (?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_AIRPLANE +
                    " SET " + COLUMN_CODE + " = ?, " + COLUMN_TYPE + " = ?, "
                    + COLUMN_FIRSTCLASSROWS + " = ?, " + COLUMN_FIRSTCLASSCOLUMNS + " = ?, "
                    + COLUMN_BUSINESSCLASSROWS + " = ?, " + COLUMN_BUSINESSCLASSCOLUMNS + " = ?, "
                    + COLUMN_ECONOMYCLASSROWS + " = ?, " + COLUMN_ECONOMYCLASSCOLUMNS + " = ?, " +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_AIRPLANE +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_AIRPLANE +
                    " WHERE id = ?;";

    private final AirplaneTableResultSetMapper mapper = new AirplaneTableResultSetMapper();
    private ConnectionPool connectionPool;

    @Autowired
    AirplaneProfileMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(AirplaneProfile entity) {
        new SqlStatement(CREATE_TEMPLATE,
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
        List<AirplaneProfile> Airplanes = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), mapper);
        if (Airplanes.isEmpty()) {
            return null;
        }
        return Airplanes.get(0);
    }

    @Override
    public List<AirplaneProfile> readAll(AirplaneProfileSpecification  specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(AirplaneProfile entity) {
        new SqlStatement(UPDATE_TEMPLATE,
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

    @Override
    public void delete(AirplaneProfile entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }
}