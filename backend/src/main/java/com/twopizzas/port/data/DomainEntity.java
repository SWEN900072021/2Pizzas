package com.twopizzas.port.data;

import com.twopizzas.data.Entity;
import com.twopizzas.domain.EntityId;
import com.twopizzas.util.AssertionConcern;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class DomainEntity extends AssertionConcern implements Entity<EntityId> {
    private boolean isNew;

    @EqualsAndHashCode.Include
    protected final EntityId id;

    public DomainEntity(EntityId id) {
        this.id = notNull(id, "id");
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public boolean isNew() {
        return isNew;
    }

    void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
