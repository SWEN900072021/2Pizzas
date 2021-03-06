package com.twopizzas.port.data.db;

import com.twopizzas.configuration.Configuration;
import com.twopizzas.configuration.Value;
import com.twopizzas.data.DataSource;
import com.twopizzas.di.ThreadLocalComponent;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ThreadLocalComponent
@Configuration
public class ConnectionPoolImpl implements DataSource, ConnectionPool {

    @Value("datasource.username")
    private String user;

    @Value("datasource.password")
    private String password;

    @Value("datasource.url")
    private String url;

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ConnectionPoolImpl() {}

    public ConnectionPoolImpl(String url, String user, String password) {
        this.password = password;
        this.user = user;
        this.url = url;
    }

    private Connection currentTransaction = null;

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
            currentTransaction = DriverManager.getConnection(url, user, password);
            currentTransaction.setAutoCommit(false);

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
