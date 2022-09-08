package de.koanam.dbtester.ia;

import de.koanam.dbtester.core.entity.TableObject;

public interface DatabaseDsGateway {

    void startDatabase(String username, String password);

    void stopDatabase();

    void createDataStructure(String statement);

    int insertContent(TableObject table);

    int clearContent(TableObject table);


}
