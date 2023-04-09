package de.koanam.dbtester.framework.jdbc;

import java.sql.*;
import java.util.*;

public class TableContentDAO {

    private static final String TABLE_PLACEHOLDER = "{:TABLE}";
    private static final String COLUMNS_PLACEHOLDER = "{:COLUMNS}";
    private static final String VALUES_PLACEHOLDER = "{:VALUES}";

    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_PLACEHOLDER +" (" + COLUMNS_PLACEHOLDER + ") VALUES (" + VALUES_PLACEHOLDER + ");";

    private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_PLACEHOLDER;

    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM " + TABLE_PLACEHOLDER;


    private Connection connection;
    private String table;

    public TableContentDAO(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    public int insertRow(Map<String, String> row) throws SQLException {
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
        }
    }

    public int deleteTable() throws SQLException {
        String stmt = DELETE_STATEMENT.replace(TABLE_PLACEHOLDER, this.table);

        try(PreparedStatement statement = this.connection.prepareStatement(stmt)){
            int count = statement.executeUpdate();
            return count;
        }
    }

    public List<Map<String, String>> getTableContent() throws SQLException {
        String stmt = SELECT_ALL_STATEMENT.replace(TABLE_PLACEHOLDER, this.table);

        try(PreparedStatement statement = this.connection.prepareStatement(stmt);
            ResultSet resultSet = statement.executeQuery()){
            List<String> columnNames = this.getColumnNames(resultSet.getMetaData());
            List<Map<String, String>> tableContent = new ArrayList<>();

            while (resultSet.next()){
                Map<String, String> row = new HashMap<>();
                for(String columnName: columnNames){
                    String value = resultSet.getString(columnName);
                    row.put(columnName, value);
                }
                tableContent.add(row);
            }

            return tableContent;
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
