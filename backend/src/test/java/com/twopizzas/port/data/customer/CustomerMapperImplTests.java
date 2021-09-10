package com.twopizzas.port.data.customer;

import com.twopizzas.domain.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class CustomerMapperImplTests {
    private CustomerMapperImpl mapper;
    private ConnectionPoolImpl connectionPool = new ConnectionPoolImpl(
            "jdbc:postgresql://ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6",
            "imvxeuqwkqsffn",
            "f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29"
    );

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
        Customer entity = new Customer(EntityId.nextId(),
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
        EntityId id = EntityId.nextId();
        Customer oldEntity = new Customer(id,
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(oldEntity);

        // WHEN
        Customer updatedEntity = new Customer(id,
                "username", "newPassword", "John", "Smith", "johnsmith@gmail.com");
        mapper.update(updatedEntity);

        // THEN
        Customer persisted = mapper.read(id);
        Assertions.assertEquals(persisted, updatedEntity);
    }

    @Test
    @DisplayName("GIVEN customer object not in db WHEN update invoked THEN customer not persisted in db")
    void testInvalidUpdate()  {
        // GIVEN
        EntityId id = EntityId.nextId();
        Customer entity = new Customer(EntityId.nextId(),
                "username", "password", "John", "Smith", "johnsmith@gmail.com");

        // WHEN
        mapper.update(entity);

        // THEN
        Customer persisted = mapper.read(id);
        Assertions.assertNull(persisted);
    }

    @Test
    @DisplayName("GIVEN airline object in db WHEN delete invoked THEN object removed from db")
    void testValidDelete() {
        // GIVEN
        EntityId id = EntityId.nextId();
        Customer entity = new Customer(EntityId.nextId(),
                "username", "password", "John", "Smith", "johnsmith@gmail.com");
        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        Customer persisted = mapper.read(id);
        Assertions.assertNull(persisted);
    }

}
