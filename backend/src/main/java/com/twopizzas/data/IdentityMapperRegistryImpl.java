package com.twopizzas.data;

public class IdentityMapperRegistryImpl implements IdentityMapperRegistry {
    @Override
    public <T extends Entity<ID>, ID, U extends IdentityMapper<T, ID>> U getForClass(Class<T> entity) {
        return null;
    }
}
