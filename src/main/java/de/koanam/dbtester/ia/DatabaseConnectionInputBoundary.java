package de.koanam.dbtester.ia;

import de.koanam.dbtester.framework.DatabaseException;

import java.util.List;

public interface DatabaseConnectionInputBoundary {

    void initDatabase(List<String> structures) throws DatabaseException;

    void stopDatabase() throws DatabaseException;

}
