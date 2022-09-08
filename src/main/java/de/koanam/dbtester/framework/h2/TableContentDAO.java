package de.koanam.dbtester.framework.h2;

import java.sql.*;
import java.util.*;

public class TableContentDAO {

    private static final String TABLE_PLACEHOLDER = "{:TABLE}";
    private static final String COLUMNS_PLACEHOLDER = "{:COLUMNS}";
    private static final String VALUES_PLACEHOLDER = "{:VALUES}";

    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_PLACEHOLDER +" (" + COLUMNS_PLACEHOLDER + ") VALUES (" + VALUES_PLACEHOLDER + ");";

    private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_PLACEHOLDER;

    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM " + TABLE_PLACEHOLDER;

    private static final String SHOW_TABLES = "SHOW TABLES";


    private Connection connection;
    private String table;

    public TableContentDAO(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    public int insertRow(Map<String, String> row){
        Collection<String> columnNames = row.keySet();
        String stmt = this.buildStatement(INSERT_STATEMENT, this.table, columnNames);

        try(PreparedStatement statement = this.connection.prepareStatement(stmt)){
            int i = 1;
            for(String columnName: columnNames){
                statement.setString(i, row.get(columnName));
                i++;
            }

            int count = statement.executeUpdate();
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteTable(){
        String stmt = DELETE_STATEMENT.replace(TABLE_PLACEHOLDER, this.table);

        try(PreparedStatement statement = this.connection.prepareStatement(stmt)){
            int count = statement.executeUpdate();
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private List<String> getColumnNames(ResultSetMetaData resultSetMetaData) throws SQLException {
        int columnCount = resultSetMetaData.getColumnCount();

        List<String> columnNames = new ArrayList<>(columnCount);
        for(int i = 1; i <= columnCount; i++){
            columnNames.add(resultSetMetaData.getColumnName(i));
        }

        return columnNames;
    }

    private String buildStatement(String statement, String tableName, Collection<String> columnNames){
        List<String> valuePlaceholder = columnNames.stream().map(c -> "?").toList();
        return statement
                .replace(TABLE_PLACEHOLDER, tableName)
                .replace(COLUMNS_PLACEHOLDER, this.buildCSVList(columnNames))
                .replace(VALUES_PLACEHOLDER, this.buildCSVList(valuePlaceholder));
    }

    private String buildCSVList(Collection<String> values){
        StringBuilder result = new StringBuilder();
        int i = 0;
        for(String v: values){
            result.append(v);

            i++;
            if(i < values.size()){
                result.append(",");
            }
        }

        return result.toString();
    }
}