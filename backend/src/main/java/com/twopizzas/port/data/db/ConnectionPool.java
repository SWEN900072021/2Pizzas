package com.twopizzas.port.data.db;

import com.twopizzas.data.DataSource;
import com.twopizzas.di.ThreadLocalComponent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ThreadLocalComponent
public class ConnectionPool implements DataSource, SqlConnectionPool {

    private final String url;

    ConnectionPool(String user, String password, String host, String port, String database) {
        url = String.format("postgres://%s:%s@%s:%s/%s", user, password, host, port, database);
    }

    public ConnectionPool(String url) {
        this.url = url;
    }

    private static Connection currentTransaction = null;

    public Connection getCurrentTransaction() {
        if (currentTransaction == null) {
            throw new ConnectionPoolTransactionException("no current transaction!!!");
        }
        return currentTransaction;
    }

    @Override
    public void startNewTransaction() {
        if (currentTransaction != null) {
            throw new ConnectionPoolTransactionException("call to start new transaction but a transaction is already in progress");
        }
        try {
            currentTransaction = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new ConnectionPoolTransactionException(String.format("failed to open connection to database, error: %s", e.getMessage()));
        }
    }

    @Override
    public void commitTransaction() {
        if (currentTransaction == null) {
            throw new ConnectionPoolTransactionException("call to commit transaction but a transaction has not been started");
        }
        try {
            currentTransaction.commit();
            currentTransaction.close();
            currentTransaction = null;
        } catch (SQLException e) {
            throw new ConnectionPoolTransactionException(String.format("failed to commit changes to database, error: %s", e.getMessage()));
        }
    }

    @Override
    public void rollbackTransaction() {
        if (currentTransaction == null) {
            throw new ConnectionPoolTransactionException("call to rollback transaction but a transaction has not been started");
        }
        try {
            currentTransaction.rollback();
            currentTransaction.close();
            currentTransaction = null;
        } catch (SQLException e) {
            throw new ConnectionPoolTransactionException(String.format("failed to rollback changes to database, error: %s", e.getMessage()));
        }
    }

    static class ConnectionPoolTransactionException extends RuntimeException {
        ConnectionPoolTransactionException(String message) {
            super(message);
        }
    }
}
