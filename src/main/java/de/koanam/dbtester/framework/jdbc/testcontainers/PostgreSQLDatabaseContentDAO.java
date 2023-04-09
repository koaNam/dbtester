package de.koanam.dbtester.framework.jdbc.testcontainers;

import de.koanam.dbtester.framework.jdbc.DatabaseContentDAO;
import de.koanam.dbtester.framework.jdbc.h2.H2DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLDatabaseContentDAO extends DatabaseContentDAO {


    private static final String SELECT_TABLE_NAMES = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public'";
    private static final String SELECT_SEQUENCE_NAMES = "SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA != 'INFORMATION_SCHEMA'";


    public PostgreSQLDatabaseContentDAO(Connection connection) {
        super(connection);
    }


    @Override
    public List<String> getAllTables() throws H2DatabaseException {
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_TABLE_NAMES);
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

    @Override
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


}
