package de.koanam.dbtester.usecase;

import de.koanam.dbtester.core.entity.DatabaseCredentialGenerator;
import de.koanam.dbtester.ia.DatabaseConnectionInputBoundary;
import de.koanam.dbtester.ia.DatabaseDsGateway;
import org.h2.tools.Server;

import java.sql.SQLException;
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
    public void initDatabase(List<String> structures) {
        String username = this.credentialGenerator.generateUsername();
        String password = this.credentialGenerator.generatePassword();

        System.out.println(username);
        System.out.println(password);

        this.databaseDsGateway.startDatabase(username, password);

        this.createDataStructures(structures);

        this.connectionPresenter.setUsername(username);
        this.connectionPresenter.setPassword(password);
    }

    @Override
    public void stopDatabase() {
        this.databaseDsGateway.stopDatabase();
    }

    private void createDataStructures(List<String> structures){
        for(String struct: structures){
            this.databaseDsGateway.createDataStructure(struct);
        }
    }
}
