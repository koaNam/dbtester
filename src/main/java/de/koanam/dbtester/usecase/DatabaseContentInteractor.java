package de.koanam.dbtester.usecase;

import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.TableParser;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.ia.DatabaseContentInputBoundary;
import de.koanam.dbtester.ia.DatabaseDsGateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class DatabaseContentInteractor implements DatabaseContentInputBoundary {

    private DatabaseDsGateway databaseDsGateway;
    private TableParser tableParser;

    public DatabaseContentInteractor() {
    }

    public DatabaseContentInteractor(DatabaseDsGateway databaseDsGateway, TableParser tableParser) {
        this.databaseDsGateway = databaseDsGateway;
        this.tableParser = tableParser;
    }

    @Override
    public void setTableParser(TableParser tableParser) {
        this.tableParser = tableParser;
    }

    @Override
    public void setDatabase(DatabaseDsGateway database) {
        this.databaseDsGateway = database;
    }

    @Override
    public void insertContent(InputStream initialTables) throws DatabaseException, IOException {
        Collection<TableObject> tables = this.tableParser.parseTables(initialTables);
        for(TableObject table: tables){
            this.databaseDsGateway.insertContent(table);
        }
    }

    @Override
    public void clearContent(InputStream input) throws DatabaseException, IOException {

    }

    protected DatabaseDsGateway getDatabaseDsGateway() {
        return databaseDsGateway;
    }

    protected TableParser getTableParser() {
        return tableParser;
    }
}
