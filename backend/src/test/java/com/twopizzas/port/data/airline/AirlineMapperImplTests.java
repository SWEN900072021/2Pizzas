package com.twopizzas.port.data.airline;

import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class AirlineMapperImplTests {
    private AirlineMapperImpl mapper;
    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        mapper = new AirlineMapperImpl(connectionPool);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid airline object WHEN create invoked THEN airline persisted in database")
    void testCreate() {
        // GIVEN
        Airline entity = new Airline("username", "password", "qantas", "QN");

        // WHEN
        mapper.create(entity);

        // THEN
        Airline persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getName(), persisted.getName());
        Assertions.assertEquals(entity.getUsername(), persisted.getUsername());
        Assertions.assertEquals(entity.getCode(), persisted.getCode());
        Assertions.assertEquals(entity.getStatus(), persisted.getStatus());
    }

    @Test
    @DisplayName("GIVEN existing airline object in db WHEN update invoked THEN airline object updated in db")
    void testValidUpdate() {
        // GIVEN
        EntityId id = EntityId.nextId();
        Airline oldEntity = new Airline(id, "airline", "password", "qantas", "QN", User.Status.ACTIVE);
        mapper.create(oldEntity);

        Airline updatedEntity = new Airline(id, "newAirline", "newPassword", "newQantas", "newQN", User.Status.INACTIVE);
        mapper.update(updatedEntity);

        Airline persisted = mapper.read(id);
        Assertions.assertEquals(persisted.getId(), updatedEntity.getId());
        Assertions.assertEquals(persisted.getUsername(), updatedEntity.getUsername());
        Assertions.assertNotNull(updatedEntity.getPassword());
        Assertions.assertEquals(persisted.getName(), updatedEntity.getName());
        Assertions.assertEquals(persisted.getCode(), updatedEntity.getCode());
        Assertions.assertEquals(persisted.getStatus(), updatedEntity.getStatus());
    }

    @Test
    @DisplayName("GIVEN airline object not in db WHEN update invoked THEN object not persisted in db")
    void testInvalidUpdate()  {
        Airline entity = new Airline("airline", "password", "qantas", "QN");
        mapper.update(entity);

        Airline persisted = mapper.read(entity.getId());
        Assertions.assertNull(persisted);
    }

    @Test
    @DisplayName("GIVEN airline object in db WHEN delete invoked THEN object removed from db")
    void testValidDelete() {
        Airline entity = new Airline("airline", "password", "qantas", "QN");
        mapper.create(entity);

        mapper.delete(entity);
        Airline persisted = mapper.read(entity.getId());
        Assertions.assertNull(persisted);
    }

}
