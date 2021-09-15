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
    private ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

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
        User customerEntity = new Customer(EntityId.nextId(),
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        User airlineEntity = new Airline(EntityId.nextId(),
                 "airline", "password", "qantas", "QN");
        User adminEntity = new Administrator(EntityId.nextId(), "admin", "password");

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
    }

    @Test
    @DisplayName("GIVEN user object in db WHEN update invoked THEN customer object updated in db")
    void testValidUpdate() {
        // GIVEN
        EntityId id1 = EntityId.nextId();
        EntityId id2 = EntityId.nextId();
        EntityId id3 = EntityId.nextId();
        User customerEntity = new Customer(id1,
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        User airlineEntity = new Airline(id2,
                "airline", "password", "qantas", "QN");
        User adminEntity = new Administrator(id3, "admin", "password");
        mapper.create(customerEntity);
        mapper.create(airlineEntity);
        mapper.create(adminEntity);

        // WHEN
        User updatedCustomerEntity = new Customer(id1,
                "username", "newPassword", "John", "Smith", "johnsmith@gmail.com");
        User updatedAirlineEntity = new Airline(id2,
                "airline", "newPassword", "qantas", "QN");
        User updatedAdminEntity = new Administrator(id3, "admin", "newPassword");
        mapper.update(updatedCustomerEntity);
        mapper.update(updatedAirlineEntity);
        mapper.update(updatedAdminEntity);

        // THEN
        User customerPersisted = mapper.read(id1);
        User airlinePersisted = mapper.read(id2);
        User adminPersisted = mapper.read(id3);
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
        EntityId id1 = EntityId.nextId();
        EntityId id2 = EntityId.nextId();
        EntityId id3 = EntityId.nextId();
        User customerEntity = new Customer(id1,
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        User airlineEntity = new Airline(id2,
                "airline", "password", "qantas", "QN");
        User adminEntity = new Administrator(id3, "admin", "password");
        mapper.create(customerEntity);
        mapper.create(airlineEntity);
        mapper.create(adminEntity);

        // WHEN
        mapper.delete(customerEntity);
        mapper.delete(airlineEntity);
        mapper.delete(adminEntity);

        // THEN
        User customerPersisted = mapper.read(id1);
        User airlinePersisted = mapper.read(id2);
        User adminPersisted = mapper.read(id3);
        Assertions.assertNull(customerPersisted);
        Assertions.assertNull(airlinePersisted);
        Assertions.assertNull(adminPersisted);
    }

    @Test
    @DisplayName("GIVEN user object in db WHEN update invoked THEN customer object updated in db")
    void testValidUpdateNoPasswordChange() {
        // GIVEN
        EntityId id = EntityId.nextId();

        User customerEntity = new Customer(id,
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(customerEntity);

        User readCustomer = mapper.read(id);

        // WHEN
        User updatedCustomerEntity = new Customer(id,
                "username", readCustomer.getPassword(), "John", "Smith", "johnsmith@gmail.com");

        mapper.update(updatedCustomerEntity);


        // THEN
        User customerPersisted = mapper.read(id);
        Assertions.assertNotNull(customerPersisted);
        Assertions.assertEquals(updatedCustomerEntity.getUsername(), customerPersisted.getUsername());
        Assertions.assertEquals(updatedCustomerEntity.getUserType(), customerPersisted.getUserType());
        Assertions.assertEquals(readCustomer.getPassword(), customerPersisted.getPassword());
    }

    @Test
    @DisplayName("GIVEN user object in db WHEN update invoked THEN customer object updated in db")
    void testValidUpdateWithPasswordChange() {
        // GIVEN
        EntityId id = EntityId.nextId();

        User customerEntity = new Customer(id,
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(customerEntity);

        User readCustomer = mapper.read(id);

        // WHEN
        User updatedCustomerEntity = new Customer(id,
                "username", "newPassword", "John", "Smith", "johnsmith@gmail.com");

        mapper.update(updatedCustomerEntity);

        // THEN
        User customerPersisted = mapper.read(id);
        Assertions.assertNotNull(customerPersisted);
        Assertions.assertEquals(updatedCustomerEntity.getUsername(), customerPersisted.getUsername());
        Assertions.assertEquals(updatedCustomerEntity.getUserType(), customerPersisted.getUserType());
        Assertions.assertNotEquals(readCustomer.getPassword(), customerPersisted.getPassword());
    }
}