package com.twopizzas.port.data.administrator;

import com.twopizzas.domain.user.Administrator;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataTestConfig;
import com.twopizzas.port.data.OptimisticLockingException;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class AdministratorMapperImplTests {
    private AdministratorMapperImpl mapper;
    private final ConnectionPoolImpl connectionPool = new DataTestConfig().getConnectionPool();

    @BeforeEach
    void setup() throws SQLException {
        mapper = new AdministratorMapperImpl(connectionPool);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid administrator object WHEN create invoked THEN administrator persisted in database")
    void testCreate() {
        // GIVEN
        Administrator entity = new Administrator("JohnSmith", "SecurePassword");

        // WHEN
        mapper.create(entity);

        // THEN
        Administrator persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getStatus(), persisted.getStatus());
    }

    @Test
    @DisplayName("GIVEN existing valid administrator object in db WHEN update invoked THEN administrator object updated")
    void testValidUpdate() {
        // GIVEN
        EntityId id = EntityId.nextId();
        Administrator oldEntity = new Administrator(id, "username", "password", User.UserStatus.ACTIVE, 0);
        mapper.create(oldEntity);

        // WHEN
        Administrator updated = new Administrator(id, "newUsername", "newPassword", User.UserStatus.INACTIVE, oldEntity.getVersion());
        mapper.update(updated);

        // THEN
        Administrator persisted = mapper.read(id);
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(updated.getId(), persisted.getId());
        Assertions.assertEquals(updated.getUsername(), persisted.getUsername());
        Assertions.assertEquals(updated.getStatus(), persisted.getStatus());
        Assertions.assertNotNull(persisted.getPassword());
        Assertions.assertEquals(updated.getVersion() + 1, persisted.getVersion());
    }

    @Test
    @DisplayName("GIVEN invalid admin version WHEN update invoked THEN throw OptimisticLockingException")
    void testInvalidUpdate() {
        // GIVEN
        Administrator oldEntity = new Administrator("username", "password");
        mapper.create(oldEntity);

        // WHEN + THEN
        Administrator updated = new Administrator(oldEntity.getId(), "newUsername", "newPassword", User.UserStatus.INACTIVE, oldEntity.getVersion() + 1) ;

        Assertions.assertThrows(OptimisticLockingException.class, () -> mapper.update(updated));

        Administrator persisted = mapper.read(oldEntity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(oldEntity.getId(), persisted.getId());
        Assertions.assertEquals(oldEntity.getUsername(), persisted.getUsername());
        Assertions.assertEquals(oldEntity.getStatus(), persisted.getStatus());
        Assertions.assertNotNull(persisted.getPassword());
    }

    @Test
    @DisplayName("GIVEN existing valid administrator object in db WHEN delete invoked THEN administrator object removed" +
            "from db") void testValidDelete() {
        // GIVEN
        EntityId id = EntityId.nextId();
        Administrator entity = new Administrator("JohnSmith", "testPW");
        mapper.create(entity);

        // WHEN
        mapper.delete(entity);

        // THEN
        Administrator persisted = mapper.read(id);
        Assertions.assertNull(persisted);
    }


}
