package com.twopizzas.port.data.db;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getCurrentTransaction();
}
