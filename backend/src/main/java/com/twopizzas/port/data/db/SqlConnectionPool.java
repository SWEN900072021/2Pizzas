package com.twopizzas.port.data.db;

import java.sql.Connection;

public interface SqlConnectionPool {
    Connection getCurrentTransaction();
}
