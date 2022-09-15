package de.koanam.dbtester.usecase;

import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.TableParser;
import de.koanam.dbtester.ia.ComparisonInputBoundary;
import de.koanam.dbtester.ia.DatabaseDsGateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class ComparisonContentInteractor implements ComparisonInputBoundary {

    private DatabaseDsGateway databaseDsGateway;
    private TableParser tableParser;
    private TableBuilderFactory tableBuilderFactory;


    public ComparisonContentInteractor(DatabaseDsGateway databaseDsGateway, TableParser tableParser, TableBuilderFactory tableBuilderFactory) {
        this.databaseDsGateway = databaseDsGateway;
        this.tableParser = tableParser;
        this.tableBuilderFactory = tableBuilderFactory;
    }

    @Override
    public boolean compare(InputStream input) {
        try {
            Collection<TableObject> otherDataset = this.tableParser.parseTables(input);
            Collection<TableObject> currentDataset = this.databaseDsGateway.getContent(this.tableBuilderFactory);

            return otherDataset.containsAll(currentDataset) && otherDataset.size() == currentDataset.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
