package de.koanam.dbtester.framework.jdbc.testcontainers;

import de.koanam.dbtester.framework.jdbc.AbstractJdbcDatabase;
import de.koanam.dbtester.framework.jdbc.DatabaseContentDAO;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;

public class PostgreSQLContainerDatabase extends AbstractJdbcDatabase {

    protected GenericContainer container;

    private static final String CONTAINER_NAME = "postgres:15.2-alpine";

    @Override
    public String getConnectionURL() {
        return "jdbc:postgresql://localhost:" + this.container.getMappedPort(5432) + "/db4testing";
    }

    @Override
    public void startDatabase(String username, String password) {
        this.container = new GenericContainer(DockerImageName.parse(CONTAINER_NAME));

        this.container.withEnv("POSTGRES_USER", username);
        this.container.withEnv("POSTGRES_PASSWORD", password);
        this.container.withEnv("POSTGRES_DB", "db4testing");

        this.container.withExposedPorts(5432);
        this.container.start();

        this.dataSource = this.initDataSource(username, password);
    }

    @Override
    public void stopDatabase() {
        this.container.stop();
    }

    private PGSimpleDataSource initDataSource(String username, String password){
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(this.getConnectionURL());
        ds.setUser(username);
        ds.setPassword(password);

        return ds;
    }

    @Override
    protected DatabaseContentDAO getDatabaseContentDAO(Connection con) {
        return new PostgreSQLDatabaseContentDAO(con);
    }
}
