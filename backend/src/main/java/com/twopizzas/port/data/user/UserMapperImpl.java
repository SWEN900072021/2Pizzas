package com.twopizzas.port.data.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.administrator.AdministratorMapper;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
class UserMapperImpl extends AbstractUserMapper<User> implements UserMapper {

    private final ConnectionPool connectionPool;
    private final CustomerMapper customerMapper;
    private final AirlineMapper airlineMapper;
    private final AdministratorMapper adminMapper;

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
        switch (entity.getUserType()) {
            case Administrator.TYPE:
                adminMapper.create((Administrator) entity);
                break;
            case Customer.TYPE:
                customerMapper.create((Customer) entity);
                break;
            case Airline.TYPE:
                airlineMapper.create((Airline) entity);
                break;
            default:
                throw new DataMappingException(String.format("no mapper found for User type %s", entity.getUserType()));
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
        switch (entity.getUserType()) {
            case Administrator.TYPE:
                adminMapper.update((Administrator) entity);
                break;
            case Airline.TYPE:
                airlineMapper.update((Airline) entity);
                break;
            case Customer.TYPE:
                customerMapper.update((Customer) entity);
                break;
            default:
                throw new DataMappingException(String.format("no mapper found for User type %s", entity.getUserType()));
        }
    }

    @Override
    public void delete(User entity) {
        switch (entity.getUserType()) {
            case Administrator.TYPE:
                adminMapper.delete((Administrator) entity);
                break;
            case Airline.TYPE:
                airlineMapper.delete((Airline) entity);
                break;
            case Customer.TYPE:
                customerMapper.delete((Customer) entity);
                break;
            default:
                throw new DataMappingException(String.format("no mapper found for User type %s", entity.getUserType()));
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
            case Administrator.TYPE:
                return adminMapper.mapOne(resultSet);
            case Customer.TYPE:
                return customerMapper.mapOne(resultSet);
            case Airline.TYPE:
                return airlineMapper.mapOne(resultSet);
            default:
                throw new DataMappingException(String.format("no mapper found for User type %s", userType));
        }
    }
}