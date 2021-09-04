package com.twopizzas.port.data.customer;

import com.twopizzas.data.DataMapper;
import com.twopizzas.data.Specification;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.User;
import com.twopizzas.port.data.user.UserSpecification;

public abstract class AbstractUserMapper<T extends User, S extends Specification<T, U>, U> implements DataMapper<T, EntityId, S> {
    public void abstractCreate(User entity){

    }

    public void abstractUpdate(User entity){

    }

    public void abstractDelete(User entity){

    }
}
