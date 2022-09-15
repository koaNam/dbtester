package de.koanam.dbtester.ia;

import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;

import java.util.Collection;

public interface DatabaseDsGateway {

    void startDatabase(String username, String password);

    void stopDatabase();

    void createDataStructure(String statement);

    int insertContent(TableObject table);

    int clearContent(TableObject table);

    Collection<TableObject> getContent(TableBuilderFactory tableBuilderFactory);

}
