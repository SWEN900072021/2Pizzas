package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.List;

public interface AdministratorRepository extends Repository<Administrator, EntityId> {
    List<Administrator> findAllAdministrators();
}
