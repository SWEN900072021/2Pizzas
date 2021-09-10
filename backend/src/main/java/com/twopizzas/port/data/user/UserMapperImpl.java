package com.twopizzas.port.data.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.*;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.administrator.AdministratorMapper;
import com.twopizzas.port.data.administrator.AdministratorMapperImpl;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.airline.AirlineMapperImpl;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.customer.CustomerMapperImpl;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public
class UserMapperImpl extends AbstractUserMapper<User> implements UserMapper {

    private ConnectionPool connectionPool;
    private CustomerMapper customerMapper;
    private AirlineMapper airlineMapper;
    private AdministratorMapper adminMapper;
    private CustomerMapperImpl customerMapperImpl;
    private AirlineMapperImpl airlineMapperImpl;
    private AdministratorMapperImpl adminMapperImpl;

    @Autowired
    UserMapperImpl(ConnectionPool connectionPool, CustomerMapper customerMapper, AirlineMapper airlineMapper, AdministratorMapper adminMapper) {
        super(connectionPool);
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
        } else {

        }
    }

    @Override
    public User read(EntityId entityId) {
        ResultSet results = abstractRead(entityId);
        return map(results).stream().findFirst().orElse(null);
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
    public List<User> map(ResultSet resultSet) {
        List<User> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                User one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", User.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }

    @Override
    public User mapOne(ResultSet resultSet) throws SQLException {
        String userType = resultSet.getObject(COLUMN_TYPE, String.class);
        switch (userType) {
            case "admin":
                return adminMapperImpl.mapOne(resultSet);
            case "customer":
                return customerMapperImpl.mapOne(resultSet);
            case "airline":
                return airlineMapperImpl.mapOne(resultSet);
        }
        return null;
    }
}