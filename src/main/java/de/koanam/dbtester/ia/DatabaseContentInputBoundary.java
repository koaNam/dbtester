package de.koanam.dbtester.ia;

import de.koanam.dbtester.core.entity.TableParser;
import de.koanam.dbtester.framework.DatabaseException;

import java.io.IOException;
import java.io.InputStream;

public interface DatabaseContentInputBoundary {

    void setTableParser(TableParser tableParser);

    void setDatabase(DatabaseDsGateway database);

    void insertContent(InputStream input) throws DatabaseException, IOException;

    void clearContent(InputStream input) throws DatabaseException, IOException;

}
