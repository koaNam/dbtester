package de.koanam.dbtester.framework.jdbc;

import de.koanam.dbtester.framework.jdbc.h2.H2DatabaseException;

import java.sql.*;
import java.util.*;

public abstract class DatabaseContentDAO {

    private static final String SEQUENCE_PLACEHOLDER = "{:SEQUENCE}";

    private static final String RESTART_SEQUENCE = "ALTER SEQUENCE " + SEQUENCE_PLACEHOLDER + " RESTART WITH ?";

    protected Connection connection;

    public DatabaseContentDAO(Connection connection) {
        this.connection = connection;
    }

    public abstract List<String> getAllTables() throws H2DatabaseException;

    public abstract List<String> getAllSequences() throws H2DatabaseException;


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
