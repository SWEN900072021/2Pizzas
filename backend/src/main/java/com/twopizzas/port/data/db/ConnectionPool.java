package com.twopizzas.port.data.db;

import com.twopizzas.data.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool implements DataSource {
    private final String user = "";
    private final String password = "";
    private final String host = "";
    private final String port = "";
    private final String database = "";
    private final String url =
            "postgres://" +
                user + ":" + password +
                "@" + host + ":" + port
                + "/" + database;

    private static ConnectionPool instance = null;
    private static Connection currentTransaction = null;

//    public static ConnectionPool getInstance() {
//        if (instance == null) {
//            instance = new ConnectionPool();
//        }
//        return instance;
//    }

    public static Connection getCurrentTransaction() {
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
        try {
            getCurrentTransaction().commit();
        } catch (SQLException e) {
            throw new ConnectionPoolTransactionException(String.format("failed to commit changes to database, error: %s", e.getMessage()));
        }
    }

    public Connection getConnection() {

        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("Cannot get connection: " + e.getMessage());
        }

        return null;

    }

    static class ConnectionPoolTransactionException extends RuntimeException {
        ConnectionPoolTransactionException(String message) {
            super(message);
        }
    }
}
