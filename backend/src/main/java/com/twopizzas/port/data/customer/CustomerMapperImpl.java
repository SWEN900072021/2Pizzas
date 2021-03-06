package com.twopizzas.port.data.customer;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.OptimisticLockingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.user.AbstractUserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerMapperImpl extends AbstractUserMapper<Customer> implements CustomerMapper  {
    static final String TABLE_USER = "\"user\"";
    static final String TABLE_CUSTOMER = "customer";
    static final String COLUMN_ID = "id";
    static final String COLUMN_GIVENNAME = "givenname";
    static final String COLUMN_SURNAME = "surname";
    static final String COLUMN_EMAIL = "email";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_CUSTOMER + "(" + COLUMN_ID + ", " + COLUMN_GIVENNAME + ", " + COLUMN_SURNAME + ", " + COLUMN_EMAIL + ")" +
                    " VALUES (?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_CUSTOMER +
                    " SET " + COLUMN_GIVENNAME + " = ?, " + COLUMN_SURNAME + " = ?, " + COLUMN_EMAIL + " = ?" +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_USER + " INNER JOIN " + TABLE_CUSTOMER +
                    " ON " + TABLE_USER + ".id =" + TABLE_CUSTOMER + ".id" +
            " WHERE " + TABLE_USER + ".id = ?;";

    private final ConnectionPool connectionPool;

    @Autowired
    public CustomerMapperImpl(ConnectionPool connectionPool) {
        super(connectionPool);
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(Customer entity) {
        abstractCreate(entity);
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getGivenName(),
                entity.getLastName(),
                entity.getEmail()).doExecute(connectionPool.getCurrentTransaction());

    }

    @Override
    public Customer read(EntityId entityId) {
        return map(new SqlStatement(SELECT_TEMPLATE,
                entityId.toString()
        ).doQuery(connectionPool.getCurrentTransaction())).stream().findFirst().orElse(null);
    }

    @Override
    public List<Customer> readAll(CustomerSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Customer entity) {
        abstractUpdate(entity);
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getGivenName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());

    }

    @Override
    public void delete(Customer entity) {
        abstractDelete(entity);
    }

    public List<Customer> map(ResultSet resultSet) {

        List<Customer> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Customer one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Customer.class.getName(), e.getMessage()),
                    e);
        }

        return mapped;
    }

    @Override
    public Customer mapOne(ResultSet resultSet) {
        try {
            return new Customer(
                    EntityId.of(resultSet.getObject(CustomerMapperImpl.COLUMN_ID, String.class)),
                    resultSet.getObject(AbstractUserMapper.COLUMN_USERNAME, String.class),
                    resultSet.getObject(AbstractUserMapper.COLUMN_PASSWORD, String.class),
                    resultSet.getObject(CustomerMapperImpl.COLUMN_GIVENNAME, String.class),
                    resultSet.getObject(CustomerMapperImpl.COLUMN_SURNAME, String.class),
                    resultSet.getObject(CustomerMapperImpl.COLUMN_EMAIL, String.class),
                    User.UserStatus.valueOf(resultSet.getObject(AbstractUserMapper.COLUMN_STATUS, String.class)),
                    resultSet.getObject(AbstractUserMapper.COLUMN_VERSION, Long.class)
            );
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Customer.class.getName(), e.getMessage()),
                    e);
        }
    }
}