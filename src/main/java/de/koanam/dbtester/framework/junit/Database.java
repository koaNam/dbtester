package de.koanam.dbtester.framework.junit;

import de.koanam.dbtester.framework.jdbc.h2.H2Database;
import de.koanam.dbtester.framework.jdbc.testcontainers.PostgreSQLContainerDatabase;
import de.koanam.dbtester.ia.DatabaseDsGateway;

public enum Database {

    PLAIN_H2(new H2Database()),
    POSTGRES_CONTAINER(new PostgreSQLContainerDatabase());

    private final DatabaseDsGateway database;

    Database(DatabaseDsGateway database){
        this.database = database;
    }

    public DatabaseDsGateway getDatabase() {
        return database;
    }
}
