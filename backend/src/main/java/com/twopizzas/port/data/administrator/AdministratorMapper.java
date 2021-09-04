package com.twopizzas.port.data.administrator;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Administrator;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface AdministratorMapper extends DataMapper<Administrator, EntityId, AdministratorSpecification>, SqlResultSetMapper<Administrator> {
    @Override
    default Class<Administrator> getEntityClass() {
        return Administrator.class;
    }
}
