package de.koanam.dbtester.ia;

import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.framework.jdbc.JDBCDatabaseConnection;

import java.util.Collection;

public interface DatabaseDsGateway {

    String getConnectionURL();
    void startDatabase(String username, String password) throws DatabaseException;

    void stopDatabase() throws DatabaseException;

    void createDataStructure(String statement) throws DatabaseException;

    int insertContent(TableObject table) throws DatabaseException;

    int clearContent(TableObject table) throws DatabaseException;

    Collection<TableObject> getContent(TableBuilderFactory tableBuilderFactory) throws DatabaseException;

    JDBCDatabaseConnection getConnection() throws DatabaseException;

}
