package de.koanam.dbtester.framework.h2;

import java.sql.*;
import java.util.*;

public class DatabaseContentDAO {

    private static final String SHOW_TABLES = "SHOW TABLES";

    private Connection connection;

    public DatabaseContentDAO(Connection connection) {
        this.connection = connection;
    }


    public List<String> getAllTables(){
        try(PreparedStatement statement = this.connection.prepareStatement(SHOW_TABLES);
            ResultSet rset = statement.executeQuery()){

            List<String> tables = new ArrayList<>();
            while(rset.next()){
                tables.add(rset.getString(1));
            }

            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
