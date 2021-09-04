package com.twopizzas.port.data.user;

import com.twopizzas.data.DataMapper;
import com.twopizzas.data.Specification;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.*;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
class UserMapperImpl implements UserMapper {
    private ConnectionPool connectionPool;
    private CustomerMapper customerMapper;
    private AirlineMapper airlineMapper;
    private AdminMapper adminMapper;

    @Autowired
    UserMapperImpl(ConnectionPool connectionPool, CustomerMapper customerMapper, AirlineMapper airlineMapper, AdminMapper adminMapper) {
        this.connectionPool = connectionPool;
        this.customerMapper = customerMapper;
        this.airlineMapper = airlineMapper;
        this.adminMapper = adminMapper;
    }

    @Override
    public void create(User entity) {
        if (entity.getUserType().equals("admin")) {
            adminMapper.create((Administrator) entity);
        } else if (entity.getUserType().equals("customer")){
            customerMapper.create((Customer) entity);
        } else if (entity.getUserType().equals("airline")){
            airlineMapper.create((Airline) entity);
        }
    }

    @Override
    public User read(EntityId entityId) {
        List<Customer> customers = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), mapper);
        if (customers.isEmpty()) {
            return null;
        }
        // maybe throw an error if there are more than one
        return customers.get(0);
    }

    @Override
    public List<User> readAll(UserSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(User entity) {
        if (entity.getUserType().equals("admin")) {
            adminMapper.update((Administrator) entity);
        }
        if (entity.getUserType().equals("airline")) {
            airlineMapper.update((Airline) entity);
        }
        if (entity.getUserType().equals("customer")) {
            customerMapper.update((Customer) entity);
        }
    }

    @Override
    public void delete(User entity) {
        if (entity.getUserType().equals("admin")) {
            adminMapper.delete((Administrator) entity);
        }
        if (entity.getUserType().equals("airline")) {
            airlineMapper.delete((Airline) entity);
        }
        if (entity.getUserType().equals("customer")) {
            customerMapper.delete((Customer) entity);
        }
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

}