package com.twopizzas.port.data.administrator;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Administrator;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministratorMapperImpl implements AdministratorMapper {
    static final String TABLE_ADMINISTRATOR = "administrator";
    static final String COLUMN_ID = "id";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_ADMINISTRATOR + "(" + COLUMN_ID + ")" +
                    " VALUES (?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_ADMINISTRATOR +
                    " SET " +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_ADMINISTRATOR +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_ADMINISTRATOR +
                    " WHERE id = ?;";

    private ConnectionPool connectionPool;

    @Autowired
    AdministratorMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(Administrator entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    public Administrator read(EntityId entityId) {
        List<Administrator> Administrators = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), this);
        if (Administrators.isEmpty()) {
            return null;
        }
        return Administrators.get(0);
    }

    @Override
    public List<Administrator> readAll(AdministratorSpecification specification) {
        return null;
    }

    @Override
    public void update(Administrator entity) {
    }

    @Override
    public void delete(Administrator entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    // placeholder
    @Override
    public List<Administrator> map(ResultSet resultSet) {
        return null;
    }

//    @Override
//    public List<Administrator> map(ResultSet resultSet) {
//        List<Administrator> mapped = new ArrayList<>();
//        try {
//            while (resultSet.next()) {
//                mapped.add(new Administrator(
//                        EntityId.of(resultSet.getObject(AdministratorMapperImpl.COLUMN_ID, String.class))
//                ));
//            }
//        } catch (SQLException e) {
//            throw new DataMappingException((String.format(
//                    "failed to map results from result set to Administrator entity, error: %s", e.getMessage()),
//                    e);
//        }
//    }
}