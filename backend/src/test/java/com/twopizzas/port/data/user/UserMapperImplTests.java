package com.twopizzas.port.data.user;

import com.twopizzas.domain.*;
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
    private ConnectionPoolImpl connectionPool = new ConnectionPoolImpl(
            "jdbc:postgresql://ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6",
            "imvxeuqwkqsffn",
            "f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29"
    );

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
        Assertions.assertEquals(customerPersisted.getPassword(), updatedCustomerEntity.getPassword());
        Assertions.assertEquals(airlinePersisted.getPassword(), updatedAirlineEntity.getPassword());
        Assertions.assertEquals(adminPersisted.getPassword(), updatedAdminEntity.getPassword());
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
}