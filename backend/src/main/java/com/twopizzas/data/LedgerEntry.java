package com.twopizzas.data;

interface LedgerEntry<T extends Entity<ID>, ID> {
    void execute(MapperRegistry registry);
}
