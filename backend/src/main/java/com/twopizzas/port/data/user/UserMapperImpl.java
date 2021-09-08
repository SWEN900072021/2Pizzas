package com.twopizzas.port.data.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.*;
import com.twopizzas.port.data.administrator.AdministratorMapper;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.customer.CustomerMapper;
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
        List<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String userType = resultSet.getObject(COLUMN_TYPE, String.class);
                if (userType.equals("admin")) {
                    //
                } else if (userType.equals("customer")) {
                    users.add(customerMapper.mapOne(resultSet));
                } else if (userType.equals("airline")) {
                    //
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User mapOne(ResultSet resultSet) {
        return null;
    }
}