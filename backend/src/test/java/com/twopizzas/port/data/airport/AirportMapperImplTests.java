package com.twopizzas.port.data.airport;

import com.twopizzas.domain.Airport;
import com.twopizzas.port.data.db.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

public class AirportMapperImplTests {

    private AirportMapperImpl mapper;
    private ConnectionPool connectionPool = new ConnectionPool(
            "postgres://imvxeuqwkqsffn:f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29@ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6"
    );

    @BeforeEach
    void setup() {
        mapper = new AirportMapperImpl(connectionPool);
        connectionPool.startNewTransaction();
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
                "COD",  "New Test Airport", "Berlin", ZoneId.of()
        );

        mapper.create();
    }
}
