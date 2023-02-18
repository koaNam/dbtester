package de.koanam.dbtester.usecase;

import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.TableParser;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.ia.DatabaseDsGateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class ClearingDatabaseContentInteractor extends DatabaseContentInteractor {

    public ClearingDatabaseContentInteractor() {
        super();
    }

    public ClearingDatabaseContentInteractor(DatabaseDsGateway databaseDsGateway, TableParser tableParser) {
        super(databaseDsGateway, tableParser);
    }

    @Override
    public void clearContent(InputStream initialTables) throws DatabaseException, IOException {
        Collection<TableObject> tablesToDelete = super.getTableParser().parseTables(initialTables);
        DatabaseDsGateway databaseDsGateway = super.getDatabaseDsGateway();

        for(TableObject tableObject: tablesToDelete){
            databaseDsGateway.clearContent(tableObject);
        }
    }
}
