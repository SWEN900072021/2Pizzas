package com.twopizzas.port.data.administrator;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Administrator;
import com.twopizzas.domain.AdministratorRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
public class AdministratorRepositoryImpl extends AbstractRepository<Administrator, EntityId, AdministratorSpecification, AdministratorMapper> implements AdministratorRepository {

    @Autowired
    public AdministratorRepositoryImpl(AdministratorMapper dataMapper) {
        super(dataMapper);
    }

}
