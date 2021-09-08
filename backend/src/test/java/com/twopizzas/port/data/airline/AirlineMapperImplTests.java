package com.twopizzas.port.data.airline;

import com.twopizzas.domain.Airline;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class AirlineMapperImplTests {
    private AirlineMapperImpl mapper;
    private ConnectionPoolImpl connectionPool = new ConnectionPoolImpl(
            "jdbc:postgresql://ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6",
            "imvxeuqwkqsffn",
            "f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29"
    );

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
    @DisplayName("GIVEN valid airline object WHEN created invoked THEN airline persisted in database")
    void testCreate() {
        // GIVEN
        Airline entity = new Airline(EntityId.nextId(), "username", "password", "qantas", "QN");

        // WHEN
        mapper.create(entity);

        // THEN
        Airline persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
    }

    @Test
    @DisplayName("GIVEN existing airline object in db WHEN update invoked THEN airline object updated in db")
    void testValidUpdate() {
        // GIVEN
        EntityId id = EntityId.nextId();
        Airline oldEntity = new Airline(id, "airline", "password", "qantas", "QN");
        mapper.create(oldEntity);

        Airline updatedEntity = new Airline(id, "airline", "newPassword", "qantas", "QN");
        mapper.update(updatedEntity);

        Airline persisted = mapper.read(id);
        Assertions.assertEquals(persisted, updatedEntity);
    }

    @Test
    @DisplayName("GIVEN airline object not in db WHEN update invoked THEN object not persisted in db")
    void testInvalidUpdate()  {
        EntityId id = EntityId.nextId();
        Airline entity = new Airline(id, "airline", "password", "qantas", "QN");
        mapper.update(entity);

        Airline persisted = mapper.read(id);
        Assertions.assertNull(persisted);
    }

    @Test
    @DisplayName("GIVEN airline object in db WHEN delete invoked THEN object removed from db")
    void testValidDelete() {
        EntityId id = EntityId.nextId();
        Airline entity = new Airline(id, "airline", "password", "qantas", "QN");
        mapper.create(entity);

        mapper.delete(entity);
        Airline persisted = mapper.read(id);
        Assertions.assertNull(persisted);
    }

}
