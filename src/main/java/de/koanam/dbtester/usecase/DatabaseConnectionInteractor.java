package de.koanam.dbtester.usecase;

import de.koanam.dbtester.core.entity.DatabaseCredentialGenerator;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.ia.DatabaseConnection;
import de.koanam.dbtester.ia.DatabaseConnectionInputBoundary;
import de.koanam.dbtester.ia.DatabaseDsGateway;

import java.util.List;

public class DatabaseConnectionInteractor implements DatabaseConnectionInputBoundary {

    private DatabaseConnectionPresenter connectionPresenter;
    private DatabaseDsGateway databaseDsGateway;
    private DatabaseCredentialGenerator credentialGenerator;

    public DatabaseConnectionInteractor(DatabaseConnectionPresenter connectionPresenter, DatabaseDsGateway databaseDsGateway, DatabaseCredentialGenerator credentialGenerator) {
        this.connectionPresenter = connectionPresenter;
        this.databaseDsGateway = databaseDsGateway;
        this.credentialGenerator = credentialGenerator;
    }

    @Override
    public void initDatabase(List<String> structures) throws DatabaseException {
        String username = this.credentialGenerator.generateUsername();
        String password = this.credentialGenerator.generatePassword();

        this.databaseDsGateway.startDatabase(username, password);

        this.createDataStructures(structures);

        this.connectionPresenter.setUsername(username);
        this.connectionPresenter.setPassword(password);
        this.connectionPresenter.setConnectionURL(this.databaseDsGateway.getConnectionURL());
    }

    @Override
    public void stopDatabase() throws DatabaseException {
        this.databaseDsGateway.stopDatabase();
    }

    @Override
    public DatabaseConnection getConnection() throws DatabaseException {
        return this.databaseDsGateway.getConnection();
    }

    private void createDataStructures(List<String> structures) throws DatabaseException {
        for(String struct: structures){
            this.databaseDsGateway.createDataStructure(struct);
        }
    }
}
