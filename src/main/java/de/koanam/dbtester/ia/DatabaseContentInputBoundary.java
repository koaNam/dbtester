package de.koanam.dbtester.ia;

import de.koanam.dbtester.framework.DatabaseException;

import java.io.IOException;
import java.io.InputStream;

public interface DatabaseContentInputBoundary {

    void insertContent(InputStream input) throws DatabaseException, IOException;

    void clearContent(InputStream input) throws DatabaseException, IOException;

}
