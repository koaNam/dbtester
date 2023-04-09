package de.koanam.dbtester.framework.jdbc;

import de.koanam.dbtester.ia.DatabaseConnection;

import java.sql.Connection;

public class JDBCDatabaseConnection implements DatabaseConnection<Connection> {

    private Connection connection;

    public JDBCDatabaseConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

}
