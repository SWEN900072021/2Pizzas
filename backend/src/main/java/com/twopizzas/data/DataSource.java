package com.twopizzas.data;

public interface DataSource {
    void startNewTransaction();
    void commitTransaction();
    void rollbackTransaction();
}
