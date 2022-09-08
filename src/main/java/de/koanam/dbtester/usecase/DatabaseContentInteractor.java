package de.koanam.dbtester.usecase;

import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.TableParser;
import de.koanam.dbtester.ia.DatabaseContentInputBoundary;
import de.koanam.dbtester.ia.DatabaseDsGateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class DatabaseContentInteractor implements DatabaseContentInputBoundary {

    private DatabaseDsGateway databaseDsGateway;
    private TableParser tableParser;

    public DatabaseContentInteractor(DatabaseDsGateway databaseDsGateway, TableParser tableParser) {
        this.databaseDsGateway = databaseDsGateway;
        this.tableParser = tableParser;
    }

    @Override
    public void insertContent(InputStream initialTables) {
        try {
            Collection<TableObject> tables = this.tableParser.parseTables(initialTables);
            for(TableObject table: tables){
                this.databaseDsGateway.insertContent(table);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearContent(InputStream input) {

    }

    public DatabaseDsGateway getDatabaseDsGateway() {
        return databaseDsGateway;
    }

    public TableParser getTableParser() {
        return tableParser;
    }
}
