package com.twopizzas.port.data;

import com.twopizzas.data.DataMapper;
import com.twopizzas.data.Entity;
import com.twopizzas.data.Specification;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.db.SqlConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlDataMapper<T extends Entity<ID>, ID extends EntityId, S extends AbstractSqlSpecification<T>> implements DataMapper<T, ID, S> {

    protected final SqlConnectionPool connectionPool;

    protected AbstractSqlDataMapper(SqlConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public abstract List<T> mapResultSet(ResultSet resultSet);

    public List<T> readAll(AbstractSqlSpecification<T> specification) {
        return specification.execute();
    }

    protected void doExecute(String statement, Object... objects) {
        try {
            prepareStatement(statement, objects).execute();
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "error executing SQL statement for %s entity, error: %s",
                    this.getEntityClass().getName(), e.getMessage()),
                    e);
        }
    }

    public ResultSet doQuery(String statement, Object... objects) {
        try {
            return prepareStatement(statement, objects).executeQuery();
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "error executing SQL query statement for %s entity, error: %s",
                    this.getEntityClass().getName(), e.getMessage()),
                    e);
        }
    }

    protected PreparedStatement prepareStatement(String template, Object... args) throws SQLException {
        PreparedStatement preparedStatement = connectionPool.getCurrentTransaction().prepareStatement(template);
        int idx = 1;
        for (Object arg: args) {
            preparedStatement.setObject(idx++, arg);
        }
        return preparedStatement;
    }

    protected PreparedStatement prepareStatement(String template, Object[] args, SQLType[] types) throws SQLException {
        if (args.length != types.length) {
            throw new IllegalArgumentException("mismatched args and stypes arrays, length of args and types must match");
        }
        PreparedStatement preparedStatement = connectionPool.getCurrentTransaction().prepareStatement(template);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i], types[i]);
        }
        return preparedStatement;
    }
}
