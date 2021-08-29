package com.twopizzas.data;

class StubEntity implements Entity<String> {

    private final String id;

    StubEntity(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
