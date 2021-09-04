package com.twopizzas.port.data.user;

import com.twopizzas.data.DataMapper;
import com.twopizzas.data.Specification;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.User;
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
    static final String TABLE_USER = "\"user\"";
    static final String COLUMN_ID = "id";
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_TYPE = "userType";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_USER + "(" + COLUMN_ID + ", " + COLUMN_USERNAME +
                    ", " + COLUMN_PASSWORD + ", " + COLUMN_TYPE + ")" + " VALUES (?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_USER +
                    " SET " + COLUMN_USERNAME + " = ?, " + COLUMN_PASSWORD + " = ?, " + COLUMN_TYPE + " = ?" +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_USER + " WHERE id = ?;";


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
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserType()
                ).doExecute(connectionPool.getCurrentTransaction());
        if (entity.getUserType().equals("admin")) {
            adminMapper.create(entity);
        } else if (entity.getUserType().equals("customer")){
            customerMapper.create((Customer) entity);
        } else if (entity.getUserType().equals("airline")){
            airlineMapper.create(entity);
        }
    }

    @Override
    public User read(EntityId entityId) {
        List<User> users = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), mapper);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public List<User> readAll(UserSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public  void update(User entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserType(),
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
        if (entity.getUserType().equals("admin")) {
            adminMapper.update(entity);
        }
        if (entity.getUserType().equals("airline")) {
            airlineMapper.update(entity);
        }
        if (entity.getUserType().equals("customer")) {
            customerMapper.update(entity);
        }
    }

    @Override
    public void delete(User entity) {
        if (entity.getUserType().equals("admin")) {
            adminMapper.delete(entity);
        }
        if (entity.getUserType().equals("airline")) {
            airlineMapper.delete(entity);
        }
        if (entity.getUserType().equals("customer")) {
            customerMapper.delete(entity);
        }
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public List<User> map(ResultSet resultSet) {
        List<User> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String type = resultSet.getObject(UserMapperImpl.COLUMN_TYPE, String.class);
                if (type.equals("admin")) {
                    return adminMapper.map(resultSet);
                }
                if (type.equals("airline")) {
                    return airlineMapper.map(resultSet);
                }
                if (type.equals("customer")) {
                    return customerMapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", User.class.getName(), e.getMessage()),
                    e);
        }
    }
}