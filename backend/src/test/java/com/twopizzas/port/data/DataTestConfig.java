package com.twopizzas.port.data;

import com.twopizzas.configuration.ConfigurationContext;
import com.twopizzas.configuration.ConfigurationContextImpl;
import com.twopizzas.port.data.db.ConnectionPoolImpl;
import com.twopizzas.util.EnvironmentUtil;

public class DataTestConfig {

    public static final String DATABASE_URL_PROPERTY = "datasource.url";
    public static final String DATABASE_USER_PROPERTY = "datasource.username";
    public static final String DATABASE_PASSWORD_PROPERTY = "datasource.password";

    private ConfigurationContext context;
    private String profile;
    private ConnectionPoolImpl connectionPool;

    public DataTestConfig() {
        profile = System.getProperty("profile");
    }

    public DataTestConfig(String profile) {
        this.profile = profile;
    }

    public ConnectionPoolImpl getConnectionPool() {
        if (context == null) {
            context = new ConfigurationContextImpl(new EnvironmentUtil(), profile);
            context.init();
        }

        if (connectionPool == null) {
            connectionPool = new ConnectionPoolImpl(
                    context.getConfigurationProperty(DATABASE_URL_PROPERTY),
                    context.getConfigurationProperty(DATABASE_USER_PROPERTY),
                    context.getConfigurationProperty(DATABASE_PASSWORD_PROPERTY)
            );
        }

        return connectionPool;
    }
}
