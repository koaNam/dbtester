package de.koanam.dbtester.framework.jdbc.h2;

import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.framework.jdbc.AbstractJdbcDatabase;
import de.koanam.dbtester.framework.jdbc.DatabaseContentDAO;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Database extends AbstractJdbcDatabase {

    private Server h2server;

    @Override
    public String getConnectionURL() {
        return "jdbc:h2:mem:db4testing;DB_CLOSE_DELAY=-1";
    }

    @Override
    public void startDatabase(String username, String password) throws DatabaseException {
        try {
            this.h2server = Server.createTcpServer();
            this.h2server.start();

            this.dataSource = this.initDataSource(username, password);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private DataSource initDataSource(String username, String password) throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        String url = this.getConnectionURL();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Override
    public void stopDatabase() throws DatabaseException {
        try(Connection connection =this.dataSource.getConnection()) {
            connection.prepareStatement("SHUTDOWN").executeUpdate();
            this.h2server = null;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected DatabaseContentDAO getDatabaseContentDAO(Connection con) {
        return new H2DatabaseContentDAO(con);
    }
}
