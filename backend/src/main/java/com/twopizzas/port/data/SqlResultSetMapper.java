package com.twopizzas.port.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface SqlResultSetMapper<T> {
    List<T> map(ResultSet resultSet);
    T mapOne(ResultSet resultSet) throws SQLException;
}
