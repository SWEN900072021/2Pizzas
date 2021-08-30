package com.twopizzas.data;

public interface IdentityMapperRegistry {
    <T extends Entity<ID>, ID, U extends IdentityMapper<T, ID>> U getForClass(Class<T> entity);
}
