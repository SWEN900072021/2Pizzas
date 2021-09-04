package com.twopizzas.port.data.customer;

import com.twopizzas.data.Entity;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.User;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.user.UserSpecification;

import java.sql.ResultSet;
import java.util.List;

@Component
class CustomerMapperImpl extends AbstractUserMapper<Customer, CustomerSpecification, ConnectionPool> implements CustomerMapper  {
    static final String TABLE_USER = "\"user\"";
    static final String TABLE_CUSTOMER = "customer";
    static final String COLUMN_ID = "id";
    static final String COLUMN_GivenName = "givenName";
    static final String COLUMN_SURNAME = "surname";
    static final String COLUMN_EMAIL = "email";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_CUSTOMER + "(" + COLUMN_ID + ", " + COLUMN_GivenName + ", " + COLUMN_SURNAME + ", " + COLUMN_EMAIL + ")" +
                    " VALUES (?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_CUSTOMER +
                    " SET " + COLUMN_GivenName + " = ?, " + COLUMN_SURNAME + " = ?, " + COLUMN_EMAIL + " = ?" +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_CUSTOMER +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_USER + " INNER JOIN " + TABLE_CUSTOMER +
                    " ON " + TABLE_USER + ".id =" + TABLE_CUSTOMER + ".id" +
            " WHERE " + TABLE_USER + ".id = ?;";

    private final CustomerTableResultSetMapper mapper = new CustomerTableResultSetMapper();
    private ConnectionPool connectionPool;

    @Autowired
    CustomerMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(Customer entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getGivenName(),
                entity.getLastName(),
                entity.getEmail()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public Customer read(EntityId entityId) {
        return null;
    }

    @Override
    public Class<Customer> getEntityClass() {
        return null;
    }

    @Override
    public List<User> readAll(UserSpecification specification) {
        return null;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void create(User entity) {

    }

    @Override
    public List<User> map(ResultSet resultSet) {
        return null;
    }
}