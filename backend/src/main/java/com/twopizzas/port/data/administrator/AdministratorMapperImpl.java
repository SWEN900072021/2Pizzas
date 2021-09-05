package com.twopizzas.port.data.administrator;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Administrator;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
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

    @Override
    public List<Administrator> map(ResultSet resultSet) {
        return null;
    }
}