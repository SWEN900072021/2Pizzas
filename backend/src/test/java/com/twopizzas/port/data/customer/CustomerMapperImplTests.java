package com.twopizzas.port.data.customer;

import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class CustomerMapperImplTests {
    private CustomerMapperImpl mapper;
    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        mapper = new CustomerMapperImpl(connectionPool);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid customer object WHEN create invoked THEN customer persisted in database")
    void testCreate() {
        // GIVEN
        Customer entity = new Customer(
                "username", "password", "John", "Smith", "johnsmith@gmail.com");

        // WHEN
        mapper.create(entity);

        // THEN
        Customer persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
    }

    @Test
    @DisplayName("GIVEN existing customer object in db WHEN update invoked THEN customer object updated in db")
    void testValidUpdate() {
        // GIVEN
        Customer oldEntity = new Customer(
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(oldEntity);

        // WHEN
        Customer updatedEntity = new Customer(oldEntity.getId(),
                "newUserName", "newPassword", "newName", "newLastName", "newjohnsmith@gmail.com", User.Status.INACTIVE);
        mapper.update(updatedEntity);

        // THEN
        Customer persisted = mapper.read(oldEntity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(persisted.getId(), updatedEntity.getId());
        Assertions.assertEquals(persisted.getUsername(), updatedEntity.getUsername());
        Assertions.assertNotNull(updatedEntity.getPassword());
        Assertions.assertEquals(persisted.getGivenName(), updatedEntity.getGivenName());
        Assertions.assertEquals(persisted.getLastName(), updatedEntity.getLastName());
        Assertions.assertEquals(persisted.getEmail(), updatedEntity.getEmail());
    }

    @Test
    @DisplayName("GIVEN customer object not in db WHEN update invoked THEN customer not persisted in db")
    void testInvalidUpdate()  {
        // GIVEN
        Customer entity = new Customer(
                "username", "password", "John", "Smith", "johnsmith@gmail.com");

        // WHEN
        mapper.update(entity);

        // THEN
        Customer persisted = mapper.read(entity.getId());
        Assertions.assertNull(persisted);
    }

    @Test
    @DisplayName("GIVEN customer object in db WHEN delete invoked THEN object removed from db")
    void testValidDelete() {
        // GIVEN
        Customer entity = new Customer(
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        Customer persisted = mapper.read(entity.getId());
        Assertions.assertNull(persisted);
    }

}
