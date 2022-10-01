package de.koanam.dbtester.framework.h2;

import java.sql.*;
import java.util.*;

public class DatabaseContentDAO {

    private static final String SEQUENCE_PLACEHOLDER = "{:TABLE}";

    private static final String SHOW_TABLES = "SHOW TABLES";
    private static final String SELECT_SEQUENCE_NAMES = "SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA != 'INFORMATION_SCHEMA'";
    private static final String RESTART_SEQUENCE = "ALTER SEQUENCE " + SEQUENCE_PLACEHOLDER + " RESTART WITH ?";

    private Connection connection;

    public DatabaseContentDAO(Connection connection) {
        this.connection = connection;
    }


    public List<String> getAllTables() throws H2DatabaseException{
        try(PreparedStatement statement = this.connection.prepareStatement(SHOW_TABLES);
            ResultSet rset = statement.executeQuery()){

            List<String> tables = new ArrayList<>();
            while(rset.next()){
                tables.add(rset.getString(1));
            }

            return tables;
        } catch (SQLException e) {
            throw new H2DatabaseException("Error while searching all tables in database", e);
        }
    }

    public List<String> getAllSequences() throws H2DatabaseException {
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_SEQUENCE_NAMES);
            ResultSet rset = statement.executeQuery()){

            List<String> tables = new ArrayList<>();
            while(rset.next()){
                tables.add(rset.getString("SEQUENCE_NAME"));
            }

            return tables;
        } catch (SQLException e) {
            throw new H2DatabaseException("Error while searching all sequences in database", e);
        }
    }

    public void restartSequence(String sequenceName, long startValue) throws H2DatabaseException{
        String stmt = RESTART_SEQUENCE.replace(SEQUENCE_PLACEHOLDER, sequenceName);
        try(PreparedStatement statement = this.connection.prepareStatement(stmt)) {
            statement.setLong(1, startValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new H2DatabaseException("Error while restarting sequence " + sequenceName + " with value " + startValue, e);
        }
    }

}
