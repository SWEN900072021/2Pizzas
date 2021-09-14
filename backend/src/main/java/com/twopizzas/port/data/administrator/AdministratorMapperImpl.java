package com.twopizzas.port.data.administrator;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.user.AbstractUserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdministratorMapperImpl extends AbstractUserMapper<Administrator> implements AdministratorMapper {
    static final String TABLE_USER = "\"user\"";
    static final String TABLE_ADMINISTRATOR = "administrator";
    static final String COLUMN_ID = "id";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_ADMINISTRATOR + "(" + COLUMN_ID + ")" +
                    " VALUES (?);";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_USER + " INNER JOIN " + TABLE_ADMINISTRATOR +
                    " ON " + TABLE_USER + ".id =" + TABLE_ADMINISTRATOR + ".id" +
                    " WHERE " + TABLE_USER + ".id = ?;";

    private ConnectionPool connectionPool;

    @Autowired
    public AdministratorMapperImpl(ConnectionPool connectionPool) {
        super(connectionPool);
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(Administrator entity) {
        abstractCreate(entity);
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
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
        abstractUpdate(entity);
    }

    @Override
    public void delete(Administrator entity) {
        abstractDelete(entity);
    }

    @Override
    public List<Administrator> map(ResultSet resultSet) {
        List<Administrator> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Administrator one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Administrator.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }

    @Override
    public Administrator mapOne(ResultSet resultSet) {
        try {
            return new Administrator(
                    EntityId.of(resultSet.getObject(AdministratorMapperImpl.COLUMN_ID, String.class)),
                    resultSet.getObject(AbstractUserMapper.COLUMN_USERNAME, String.class),
                    resultSet.getObject(AbstractUserMapper.COLUMN_PASSWORD, String.class)
            );
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Administrator.class.getName(), e.getMessage()),
                    e);
        }
    }
}