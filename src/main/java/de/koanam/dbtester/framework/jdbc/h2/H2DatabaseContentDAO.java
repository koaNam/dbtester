package de.koanam.dbtester.framework.jdbc.h2;

import de.koanam.dbtester.framework.jdbc.DatabaseContentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2DatabaseContentDAO extends DatabaseContentDAO {


    private static final String SHOW_TABLES = "SHOW TABLES";
    private static final String SELECT_SEQUENCE_NAMES = "SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA != 'INFORMATION_SCHEMA'";


    public H2DatabaseContentDAO(Connection connection) {
        super(connection);
    }


    @Override
    public List<String> getAllTables() throws H2DatabaseException {
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
