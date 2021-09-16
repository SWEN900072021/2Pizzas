package com.twopizzas.port.data.administrator;

import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.domain.user.AdministratorRepository;
import com.twopizzas.domain.EntityId;

@Component
class AdministratorRepositoryImpl extends AbstractRepository<Administrator, AdministratorSpecification, AdministratorMapper> implements AdministratorRepository {

    @Autowired
    public AdministratorRepositoryImpl(AdministratorMapper dataMapper) {
        super(dataMapper);
    }

}
