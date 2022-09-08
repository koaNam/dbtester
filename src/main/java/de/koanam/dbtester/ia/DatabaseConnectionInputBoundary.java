package de.koanam.dbtester.ia;

import java.util.List;

public interface DatabaseConnectionInputBoundary {

    void initDatabase(List<String> structures); //TODO Einheitlich auf String/InputStream ändern

    void stopDatabase();

}
