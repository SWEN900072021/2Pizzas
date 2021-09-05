package com.twopizzas.port.data.administrator;

import com.twopizzas.domain.Administrator;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.administrator.AdministratorMapperImpl;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class AdministratorMapperImplTests {
    private AdministratorMapperImpl mapper;
    private ConnectionPoolImpl connectionPool = new ConnectionPoolImpl(
            "jdbc:postgresql://ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6",
            "imvxeuqwkqsffn",
            "f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29"
    );

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
    @DisplayName("GIVEN valid administrator object WHEN created invoked THEN administrator persisted in database")
    void test() {
        // GIVEN
        Administrator entity = new Administrator("John", "Smith");

        // WHEN
        mapper.create(entity);

        // THEN
        Administrator persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
    }
}
