package com.twopizzas.port.data;

import com.twopizzas.data.Entity;

import java.sql.ResultSet;
import java.util.List;

public interface SqlDataMapper<T extends Entity<ID>, ID> {
    List<T> mapResultSet(ResultSet resultSet);
    ResultSet doQuery(String statement, Object... objects);
}
