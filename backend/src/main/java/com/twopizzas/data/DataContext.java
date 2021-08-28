package com.twopizzas.data;

public interface DataContext {
    UnitOfWork getUnitOfWork();
    IdentityMapper getIdentityMapper();
    MapperRegistry getMapperRegistry();
    DataSource getDataSource();
}
