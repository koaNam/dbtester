package de.koanam.dbtester.framework.h2;

import de.koanam.dbtester.core.entity.TableBuilder;
import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.ia.DatabaseDsGateway;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class H2Database implements DatabaseDsGateway {

    private Server h2server;
    private DataSource dataSource;

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
    public void createDataStructure(String statement) throws DatabaseException {
        try(Connection connection =this.dataSource.getConnection()) {
            this.executeDDLStatement(statement, connection);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int insertContent(TableObject table) throws DatabaseException {
        List<Map<String, String>> tableContent = table.getContent();
        try(Connection connection = this.dataSource.getConnection()){
            TableContentDAO tableContentDAO = new TableContentDAO(connection, table.getTableName());
            this.insertContent(tableContent, tableContentDAO);
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return tableContent.size();
    }

    @Override
    public int clearContent(TableObject table) throws DatabaseException {
        try(Connection connection = this.dataSource.getConnection()){
            this.restartSequences(connection);

            int deleteCount = this.deleteTableContent(table, connection);
            return deleteCount;
        } catch (SQLException | H2DatabaseException e) {
            throw new DatabaseException(e);
        }
    }

    private void restartSequences(Connection connection) throws H2DatabaseException {
        DatabaseContentDAO databaseContentDAO = new DatabaseContentDAO(connection);
        List<String> sequenceNames = databaseContentDAO.getAllSequences();
        for(String sequenceName: sequenceNames){
            databaseContentDAO.restartSequence(sequenceName, 0);
        }
    }

    private int deleteTableContent(TableObject table, Connection connection) throws SQLException {
        TableContentDAO tableContentDAO = new TableContentDAO(connection, table.getTableName());

        int deleteCount = tableContentDAO.deleteTable();
        connection.commit();
        return deleteCount;
    }

    public List<TableObject> getContent(TableBuilderFactory tableBuilderFactory) throws DatabaseException {
        try(Connection connection = this.dataSource.getConnection()){
            DatabaseContentDAO databaseContentDAO = new DatabaseContentDAO(connection);
            List<String> tableNames = databaseContentDAO.getAllTables();

            List<TableObject> result = new ArrayList<>();
            for(String tableName: tableNames){
                TableContentDAO tableContentDAO = new TableContentDAO(connection, tableName);
                List<Map<String,String>> content = tableContentDAO.getTableContent();

                TableObject tableObject = this.buildTableObject(tableBuilderFactory, tableName, content);
                result.add(tableObject);
            }

            return result;
        } catch (SQLException | H2DatabaseException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public JDBCDatabaseConnection getConnection() throws DatabaseException {
        try {
            return new JDBCDatabaseConnection(this.dataSource.getConnection());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private TableObject buildTableObject(TableBuilderFactory tableBuilderFactory, String tableName, List<Map<String, String>> content) {
        TableBuilder tableBuilder = tableBuilderFactory.getBuilder();
        tableBuilder.setName(tableName);
        for(Map<String, String> row: content){
            List<String> columnNames = new ArrayList<>(row.keySet());
            List<String> rowValues = columnNames.stream().map(c -> row.get(c)).toList();

            tableBuilder.setColumnNames(columnNames);
            tableBuilder.addRow(rowValues);
        }

        return tableBuilder.build();
    }

    private void insertContent(List<Map<String, String>> values, TableContentDAO tableContentDAO) throws SQLException {
        for(Map<String, String> value: values){
            tableContentDAO.insertRow(value);
        }
    }

    private void executeDDLStatement(String statement, Connection con) throws SQLException {
        con.prepareStatement(statement).executeUpdate();
    }

}
