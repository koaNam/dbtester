package de.koanam.dbtester.framework.h2;

import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.ia.DatabaseDsGateway;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class H2Database implements DatabaseDsGateway {

    private Server h2server;
    private DataSource dataSource;

    @Override
    public void startDatabase(String username, String password) {
        try {
            this.h2server = Server.createTcpServer();
            this.h2server.start();

            this.dataSource = this.initDataSource(username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DataSource initDataSource(String username, String password) throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:db4testing;DB_CLOSE_DELAY=-1");
        dataSource.setUser(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Override
    public void stopDatabase() {
        try(Connection connection =this.dataSource.getConnection()) {
            connection.prepareStatement("SHUTDOWN").executeUpdate();
            this.h2server = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);  //TODO Exception anpassen
        }
    }

    @Override
    public void createDataStructure(String statement) {
        try(Connection connection =this.dataSource.getConnection()) {
            this.executeDDLStatement(statement, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insertContent(TableObject table) {
        List<Map<String, String>> tableContent = table.getContent();
        try(Connection connection = this.dataSource.getConnection()){
            TableContentDAO tableContentDAO = new TableContentDAO(connection, table.getTableName());
            this.insertContent(tableContent, tableContentDAO);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableContent.size();
    }

    @Override
    public int clearContent(TableObject table) {
        try(Connection connection = this.dataSource.getConnection()){
            TableContentDAO tableContentDAO = new TableContentDAO(connection, table.getTableName());

            int deleteCount = tableContentDAO.deleteTable();
            connection.commit();

            return deleteCount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertContent(List<Map<String, String>> values, TableContentDAO tableContentDAO){
        for(Map<String, String> value: values){
            tableContentDAO.insertRow(value);
        }
    }

    private void executeDDLStatement(String statement, Connection con) throws SQLException {
        con.prepareStatement(statement).executeUpdate();
    }

}
