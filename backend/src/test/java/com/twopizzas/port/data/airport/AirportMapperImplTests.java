package com.twopizzas.port.data.airport;

import com.twopizzas.domain.Airport;
import com.twopizzas.port.data.db.ConnectionPool;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;

public class AirportMapperImplTests {

    private AirportMapperImpl mapper;
    private ConnectionPool connectionPool = new ConnectionPool(
            "jdbc:postgresql://ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6",
            "imvxeuqwkqsffn",
            "f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29"

    );

    @BeforeEach
    void setup() throws SQLException {
        mapper = new AirportMapperImpl(connectionPool);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }

    @Test
    @DisplayName("GIVEN valid airport object WHEN created invoked THEN airport persisted in database")
    void test() {
        // GIVEN
        Airport entity = new Airport(
                "COD",  "New Test Airport", "Berlin", ZoneId.of("Asia/Calcutta")
        );

        // WHEN
        mapper.create(entity);

        // THEN
        Airport persisted = mapper.read(entity.getId());
        Assertions.assertNotNull(persisted);
        Assertions.assertEquals(entity.getId(), persisted.getId());
        Assertions.assertEquals(entity.getCode(), persisted.getCode());
        Assertions.assertEquals(entity.getName(), persisted.getName());
        Assertions.assertEquals(entity.getLocation(), persisted.getLocation());
        Assertions.assertEquals(entity.getUtcOffset(), persisted.getUtcOffset());
    }
}
