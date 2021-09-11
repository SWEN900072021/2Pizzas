package com.twopizzas.port.data;

import com.twopizzas.port.data.db.ConnectionPoolImpl;

public class DataTestConfig {

    private final ConnectionPoolImpl connectionPool = new ConnectionPoolImpl(
            "jdbc:postgresql://ec2-35-153-114-74.compute-1.amazonaws.com:5432/dac5q82fjaj3t6",
            "imvxeuqwkqsffn",
            "f4ed9811c5e77c79fc4ac9bae81de7b24ede0452ea454a656ba916c17a347f29"
    );

    public ConnectionPoolImpl getConnectionPool() {
        return connectionPool;
    }
}
