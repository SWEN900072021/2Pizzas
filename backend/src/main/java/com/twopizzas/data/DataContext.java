package com.twopizzas.data;

public interface DataContext {
    UnitOfWork getUnitOfWork();
    IdentityMapperRegistry getIdentityMapperRegistry();
    DataMapperRegistry getDataMapperRegistry();
    DataSource getDataSource();
}
