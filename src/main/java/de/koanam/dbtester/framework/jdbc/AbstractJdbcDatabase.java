package de.koanam.dbtester.framework.jdbc;

import de.koanam.dbtester.core.entity.TableBuilder;
import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.framework.jdbc.h2.H2DatabaseException;
import de.koanam.dbtester.ia.DatabaseDsGateway;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractJdbcDatabase implements DatabaseDsGateway {

    protected DataSource dataSource;

    @Override
    public JDBCDatabaseConnection getConnection() throws DatabaseException {
        try {
            Connection connection = this.getConnectionFromDataSource();
            return new JDBCDatabaseConnection(connection);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private Connection getConnectionFromDataSource() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }


    @Override
    public void createDataStructure(String statement) throws DatabaseException {
        try(Connection connection =this.getConnectionFromDataSource()) {
            this.executeDDLStatement(statement, connection);
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int insertContent(TableObject table) throws DatabaseException {
        List<Map<String, String>> tableContent = table.getContent();
        try(Connection connection = this.getConnectionFromDataSource()){
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
        try(Connection connection = this.getConnectionFromDataSource()){
            this.restartSequences(connection);

            int deleteCount = this.deleteTableContent(table, connection);
            connection.commit();
            return deleteCount;
        } catch (SQLException | H2DatabaseException e) {
            throw new DatabaseException(e);
        }
    }

    private void restartSequences(Connection connection) throws H2DatabaseException {
        DatabaseContentDAO databaseContentDAO = this.getDatabaseContentDAO(connection);
        List<String> sequenceNames = databaseContentDAO.getAllSequences();
        for(String sequenceName: sequenceNames){
            databaseContentDAO.restartSequence(sequenceName, 0);
        }
    }

    private int deleteTableContent(TableObject table, Connection connection) throws SQLException {
        TableContentDAO tableContentDAO = new TableContentDAO(connection, table.getTableName());

        int deleteCount = tableContentDAO.deleteTable();
        return deleteCount;
    }

    @Override
    public List<TableObject> getContent(TableBuilderFactory tableBuilderFactory) throws DatabaseException {
        try(Connection connection = this.dataSource.getConnection()){
            DatabaseContentDAO databaseContentDAO = this.getDatabaseContentDAO(connection);
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

    protected abstract DatabaseContentDAO getDatabaseContentDAO(Connection con);
}
