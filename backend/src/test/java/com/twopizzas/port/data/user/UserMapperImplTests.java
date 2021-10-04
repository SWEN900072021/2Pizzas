package com.twopizzas.port.data.user;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.airline.AirlineMapperImpl;
import com.twopizzas.port.data.administrator.AdministratorMapperImpl;
import com.twopizzas.port.data.customer.CustomerMapperImpl;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class UserMapperImplTests {
    private UserMapperImpl mapper;
    private AdministratorMapperImpl administratorMapper;
    private AirlineMapperImpl airlineMapper;
    private CustomerMapperImpl customerMapper;
    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        administratorMapper = new AdministratorMapperImpl(connectionPool);
        airlineMapper = new AirlineMapperImpl(connectionPool);
        customerMapper = new CustomerMapperImpl(connectionPool);
        mapper = new UserMapperImpl(connectionPool, customerMapper, airlineMapper, administratorMapper);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid user object WHEN created invoked THEN user persisted in database")
    void testCreate() {
        // GIVEN
        User customerEntity = new Customer("username", "password", "John", "Smith", "johnsmith@gmail.com");
        User airlineEntity = new Airline("airline", "password", "qantas", "QN");
        User adminEntity = new Administrator( "admin", "password");

        // WHEN
        mapper.create(customerEntity);
        mapper.create(airlineEntity);
        mapper.create(adminEntity);

        // THEN
        User customerPersisted = mapper.read(customerEntity.getId());
        User airlinePersisted = mapper.read(airlineEntity.getId());
        User adminPersisted = mapper.read(adminEntity.getId());
        Assertions.assertNotNull(customerPersisted);
        Assertions.assertNotNull(airlinePersisted);
        Assertions.assertNotNull(adminPersisted);
        Assertions.assertEquals(customerEntity.getId(), customerPersisted.getId());
        Assertions.assertEquals(airlineEntity.getId(), airlinePersisted.getId());
        Assertions.assertEquals(adminEntity.getId(), adminPersisted.getId());
        Assertions.assertEquals(customerPersisted.getUsername(), customerEntity.getUsername());
        Assertions.assertEquals(airlinePersisted.getUsername(), airlineEntity.getUsername());
        Assertions.assertEquals(adminPersisted.getUsername(), adminEntity.getUsername());
        Assertions.assertNotNull(customerPersisted.getPassword());
        Assertions.assertNotNull(airlinePersisted.getPassword());
        Assertions.assertNotNull(adminPersisted.getPassword());
        Assertions.assertEquals(customerPersisted.getStatus(), customerEntity.getStatus());
        Assertions.assertEquals(airlinePersisted.getStatus(), airlineEntity.getStatus());
        Assertions.assertEquals(adminPersisted.getStatus(), adminEntity.getStatus());
    }

    @Test
    @DisplayName("GIVEN user object in db WHEN update invoked THEN customer object updated in db")
    void testValidUpdate() {
        // GIVEN
        User customerEntity = new Customer(EntityId.nextId(), "username", "password", "John", "Smith", "johnsmith@gmail.com", User.UserStatus.ACTIVE);
        User airlineEntity = new Airline(EntityId.nextId(), "airline", "password", "qantas", "QN", User.UserStatus.ACTIVE);
        User adminEntity = new Administrator(EntityId.nextId(), "admin", "password", User.UserStatus.ACTIVE);
        mapper.create(customerEntity);
        mapper.create(airlineEntity);
        mapper.create(adminEntity);

        // WHEN
        User updatedCustomerEntity = new Customer(customerEntity.getId(), "username", "newPassword", "John", "Smith", "johnsmith@gmail.com", User.UserStatus.INACTIVE);
        User updatedAirlineEntity = new Airline(airlineEntity.getId(), "airline", "newPassword", "qantas", "QN", User.UserStatus.INACTIVE);
        User updatedAdminEntity = new Administrator(adminEntity.getId(), "admin", "newPassword", User.UserStatus.INACTIVE);
        mapper.update(updatedCustomerEntity);
        mapper.update(updatedAirlineEntity);
        mapper.update(updatedAdminEntity);

        // THEN
        User customerPersisted = mapper.read(customerEntity.getId());
        User airlinePersisted = mapper.read(airlineEntity.getId());
        User adminPersisted = mapper.read(adminEntity.getId());
        Assertions.assertNotNull(customerPersisted);
        Assertions.assertNotNull(airlinePersisted);
        Assertions.assertNotNull(adminPersisted);
        Assertions.assertEquals(customerPersisted.getId(), updatedCustomerEntity.getId());
        Assertions.assertEquals(airlinePersisted.getId(), updatedAirlineEntity.getId());
        Assertions.assertEquals(adminPersisted.getId(), updatedAdminEntity.getId());
        Assertions.assertEquals(customerPersisted.getUsername(), updatedCustomerEntity.getUsername());
        Assertions.assertEquals(airlinePersisted.getUsername(), updatedAirlineEntity.getUsername());
        Assertions.assertEquals(adminPersisted.getUsername(), updatedAdminEntity.getUsername());
        Assertions.assertNotNull(updatedCustomerEntity.getPassword());
        Assertions.assertNotNull(updatedAirlineEntity.getPassword());
        Assertions.assertNotNull(updatedAdminEntity.getPassword());
    }

    @Test
    @DisplayName("GIVEN user object in db WHEN delete invoked THEN object removed from db")
    void testValidDelete() {
        // GIVEN
        User customerEntity = new Customer("username", "password", "John", "Smith", "johnsmith@gmail.com");
        User airlineEntity = new Airline("airline", "password", "qantas", "QN");
        User adminEntity = new Administrator( "admin", "password");
        mapper.create(customerEntity);
        mapper.create(airlineEntity);
        mapper.create(adminEntity);

        // WHEN
        mapper.delete(customerEntity);
        mapper.delete(airlineEntity);
        mapper.delete(adminEntity);

        // THEN
        User customerPersisted = mapper.read(customerEntity.getId());
        User airlinePersisted = mapper.read(airlineEntity.getId());
        User adminPersisted = mapper.read(adminEntity.getId());
        Assertions.assertNull(customerPersisted);
        Assertions.assertNull(airlinePersisted);
        Assertions.assertNull(adminPersisted);
    }

    @Test
    @DisplayName("GIVEN user object in db WHEN update invoked THEN customer object updated in db")
    void testValidUpdateNoPasswordChange() {
        // GIVEN

        User customerEntity = new Customer("username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(customerEntity);

        User readCustomer = mapper.read(customerEntity.getId());

        // WHEN
        User updatedCustomerEntity = new Customer(customerEntity.getId(),
                "username", readCustomer.getPassword(), "John", "Smith", "johnsmith@gmail.com", customerEntity.getStatus());

        mapper.update(updatedCustomerEntity);


        // THEN
        User customerPersisted = mapper.read(customerEntity.getId());
        Assertions.assertNotNull(customerPersisted);
        Assertions.assertEquals(updatedCustomerEntity.getUsername(), customerPersisted.getUsername());
        Assertions.assertEquals(updatedCustomerEntity.getUserType(), customerPersisted.getUserType());
        Assertions.assertEquals(readCustomer.getPassword(), customerPersisted.getPassword());
    }

    @Test
    @DisplayName("GIVEN user object in db WHEN update invoked THEN customer object updated in db")
    void testValidUpdateWithPasswordChange() {
        // GIVEN
        User customerEntity = new Customer(
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(customerEntity);

        User readCustomer = mapper.read(customerEntity.getId());

        // WHEN
        User updatedCustomerEntity = new Customer(customerEntity.getId(),
                "username", "newPassword", "John", "Smith", "johnsmith@gmail.com", customerEntity.getStatus());

        mapper.update(updatedCustomerEntity);

        // THEN
        User customerPersisted = mapper.read(customerEntity.getId());
        Assertions.assertNotNull(customerPersisted);
        Assertions.assertEquals(updatedCustomerEntity.getUsername(), customerPersisted.getUsername());
        Assertions.assertEquals(updatedCustomerEntity.getUserType(), customerPersisted.getUserType());
        Assertions.assertNotEquals(readCustomer.getPassword(), customerPersisted.getPassword());
    }
}