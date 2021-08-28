package com.twopizzas.data;

interface MapperRegistry {
    <ID, U extends Entity<ID>, T extends DataMapper<U, ID, ?>> T getForClass(Class<U> entity);
    <ID, U extends Entity<ID>, T extends DataMapper<U, ID, ?>> void register(Class<U> entity, T dataMapper);
}
