package com.twopizzas.data;

interface DataMapperRegistry {
    <T extends Entity<ID>, ID, U extends DataMapper<T, ID, S>, S extends Specification<T>> U getForClass(Class<T> entity);
    <T extends Entity<ID>, ID, U extends DataMapper<T, ID, S>, S extends Specification<T>> void register(Class<T> entity, U dataMapper);
}
