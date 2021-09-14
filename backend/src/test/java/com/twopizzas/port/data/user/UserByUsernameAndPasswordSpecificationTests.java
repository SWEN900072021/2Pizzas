package com.twopizzas.port.data.user;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.administrator.AdministratorMapper;
import com.twopizzas.port.data.administrator.AdministratorMapperImpl;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.airline.AirlineMapperImpl;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.customer.CustomerMapperImpl;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

public class UserByUsernameAndPasswordSpecificationTests {

    private UserMapper mapper;

    @BeforeEach
    void setup() throws SQLException {
        ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();
        AdministratorMapper administratorMapper = new AdministratorMapperImpl(connectionPool);
        AirlineMapper airlineMapper = new AirlineMapperImpl(connectionPool);
        CustomerMapper customerMapper = new CustomerMapperImpl(connectionPool);
        mapper = new UserMapperImpl(connectionPool, customerMapper, airlineMapper, administratorMapper);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @Test
    @DisplayName("GIVEN user in database with username and password WHEN find THEN return that user")
    void test() {
        // GIVEN
        User customerEntity = new Customer(EntityId.nextId(),
                "username", "password", "John", "Smith", "johnsmith@gmail.com");

        mapper.create(customerEntity);

        // WHEN
        UserSpecification specification = new UserByUsernameAndPasswordSpecification(mapper, customerEntity.getUsername(), customerEntity.getPassword());
        List<User> users = mapper.readAll(specification);

        // THEN
        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(customerEntity, users.get(0));
    }
}
