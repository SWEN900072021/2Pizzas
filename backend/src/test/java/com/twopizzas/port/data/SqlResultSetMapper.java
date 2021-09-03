package com.twopizzas.port.data;

import java.sql.ResultSet;
import java.util.List;

public interface SqlResultSetMapper<T> {
    List<T> map(ResultSet resultSet);
}
