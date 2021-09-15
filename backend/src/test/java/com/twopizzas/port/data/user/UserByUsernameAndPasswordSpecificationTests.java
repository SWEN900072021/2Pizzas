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
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

public class UserByUsernameAndPasswordSpecificationTests {

    private UserMapper mapper;
    private ConnectionPoolImpl connectionPool;

    @BeforeEach
    void setup() throws SQLException {
        connectionPool = new DataTestConfig().getConnectionPool();
        AdministratorMapper administratorMapper = new AdministratorMapperImpl(connectionPool);
        AirlineMapper airlineMapper = new AirlineMapperImpl(connectionPool);
        CustomerMapper customerMapper = new CustomerMapperImpl(connectionPool);
        mapper = new UserMapperImpl(connectionPool, customerMapper, airlineMapper, administratorMapper);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
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

    @Test
    @DisplayName("GIVEN user in database with username and password WHEN find with incorrect password THEN return empty")
    void test2() {
        // GIVEN
        User customerEntity = new Customer(EntityId.nextId(),
                "username", "password", "John", "Smith", "johnsmith@gmail.com");

        mapper.create(customerEntity);

        // WHEN
        UserSpecification specification = new UserByUsernameAndPasswordSpecification(mapper, customerEntity.getUsername(), "wrong");
        List<User> users = mapper.readAll(specification);

        // THEN
        Assertions.assertNotNull(users);
        Assertions.assertTrue(users.isEmpty());
    }
}
