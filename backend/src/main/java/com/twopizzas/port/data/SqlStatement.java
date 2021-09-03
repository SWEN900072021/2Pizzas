package com.twopizzas.port.data;

import java.sql.*;
import java.util.List;

public class SqlStatement {
    private final String template;
    private final Object[] args;

    public SqlStatement(String template, Object... args) {
        this.template = template;
        this.args = args;
    }

    public SqlStatement(String template) {
        this(template, new Object[]{});
    }

    public void doExecute(Connection connection) {
        try {
            prepareStatement(connection, template, args).execute();
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "error executing SQL statement %s, error: %s",
                    template, e.getMessage()),
                    e);
        }
    }

    public ResultSet doQuery(Connection connection) {
        try {
            return prepareStatement(connection, template, args).executeQuery();
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "error executing SQL query statement %s, error: %s",
                    template, e.getMessage()),
                    e);
        }
    }

    public <T> List<T> doQuery(Connection connection, SqlResultSetMapper<T> resultSetMapper) {
        try {
            return resultSetMapper.map(prepareStatement(connection, template, args).executeQuery());
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "error executing SQL query statement %s, error: %s",
                    template, e.getMessage()),
                    e);
        }
    }

    private PreparedStatement prepareStatement(Connection connection, String template, Object[] args) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(template);
        int idx = 1;
        for (Object arg: args) {
            preparedStatement.setObject(idx++, arg);
        }
        return preparedStatement;
    }
}
