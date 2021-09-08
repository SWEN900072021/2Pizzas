package com.twopizzas.port.data.user;

import com.twopizzas.domain.Administrator;
import com.twopizzas.domain.User;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.administrator.AdministratorMapper;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.user.UserMapperImpl;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class UserMapperImplTests {
    private UserMapperImpl mapper;
    private AdministratorMapper administratorMapper;
    private AirlineMapper airlineMapper;
    private CustomerMapper customerMapper;
    private ConnectionPoolImpl connectionPool = new ConnectionPoolImpl(
            "jdbc:postgresql://ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6",
            "imvxeuqwkqsffn",
            "f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29"
    );

    @BeforeEach
    void setup() throws SQLException {
        mapper = new UserMapperImpl(connectionPool, customerMapper, airlineMapper, administratorMapper);
        connectionPool.startNewTransaction();
        connectionPool.getCurrentTransaction().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() {
        connectionPool.rollbackTransaction();
    }


}