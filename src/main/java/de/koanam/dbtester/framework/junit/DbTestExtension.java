package de.koanam.dbtester.framework.junit;

import de.koanam.dbtester.core.entity.ContentDifference;
import de.koanam.dbtester.core.entity.TableParser;
import de.koanam.dbtester.core.entity.generic.GenericDatabaseCredentialGenerator;
import de.koanam.dbtester.core.entity.generic.GenericTableBuilderFactory;
import de.koanam.dbtester.core.entity.generic.MarkdownTableParser;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.framework.DatabaseInteractionException;
import de.koanam.dbtester.framework.InitializationException;
import de.koanam.dbtester.framework.jdbc.h2.H2Database;
import de.koanam.dbtester.ia.*;
import de.koanam.dbtester.usecase.*;
import org.junit.jupiter.api.extension.*;
import org.opentest4j.AssertionFailedError;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class DbTestExtension implements AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private boolean initialized = false;
    private List<String> initialStatements;

    private String currentInitialDatasetContent;

    private DatabaseDsGateway database = new H2Database();
    private TableParser tableParser = new MarkdownTableParser(new GenericTableBuilderFactory());

    private DatabaseConnectionPresenter databaseConnectionPresenter = new GenericDatabaseConnectionPresenter();

    private DatabaseConnectionInputBoundary databaseConnectionUseCase;

    private DatabaseContentInputBoundary databaseContentUseCase = new ClearingDatabaseContentInteractor(
            database,
            this.tableParser
    );

    private ComparisonInputBoundary comparisonUseCase = new ComparisonContentInteractor(
            database,
            this.tableParser,
            new GenericTableBuilderFactory()
    );

    @Override
    public void afterAll(ExtensionContext extensionContext) throws DatabaseException {
        this.databaseConnectionUseCase.stopDatabase();
        this.initialized = false;
        this.initialStatements = null;
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        try {
            if (this.currentInitialDatasetContent != null) {
                ByteArrayInputStream initialDataset = new ByteArrayInputStream(this.currentInitialDatasetContent.getBytes());
                this.databaseContentUseCase.clearContent(initialDataset);
            }
        } catch (IOException | DatabaseException e) {
            throw new DatabaseInteractionException(e);
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        this.setUp();
    }

    void setDatabaseContentUseCase(DatabaseContentInputBoundary contentUseCase){
        this.databaseContentUseCase = contentUseCase;
        this.databaseContentUseCase.setDatabase(this.database);
        this.databaseContentUseCase.setTableParser(this.tableParser);
    }

    void setTableParser(TableParser tableParser){
        this.tableParser = tableParser;
    }

    void setDatabase(DatabaseDsGateway database){
        this.database = database;
    }

    void insertContent() {
        try {
            ByteArrayInputStream initialDataset = new ByteArrayInputStream(this.currentInitialDatasetContent.getBytes());
            this.databaseContentUseCase.insertContent(initialDataset);
        } catch (DatabaseException | IOException e) {
            throw new DatabaseInteractionException(e);
        }
    }

    void setInitialStatements(List<String> initialStatements) {
        this.initialStatements = initialStatements;
    }

    void setCurrentInitialDatasetContent(String currentInitialDatasetContent) {
        this.currentInitialDatasetContent = currentInitialDatasetContent;
    }

     void assertEqualDataset(String expectedDatasetContent) {
        try {
            ByteArrayInputStream expectedDataset = new ByteArrayInputStream(expectedDatasetContent.getBytes());

            Collection<ContentDifference> differences = this.comparisonUseCase.compare(expectedDataset);
            if (!differences.isEmpty()) {
                throw new AssertionFailedError(this.formatErrorMessage(differences));
            }
        } catch (DatabaseException | IOException e) {
            throw new DatabaseInteractionException(e);
        }
    }

    String getUser(){
        this.checkInitialization();
        return databaseConnectionPresenter.getUsername();
    }

    String getPassword(){
        this.checkInitialization();
        return databaseConnectionPresenter.getPassword();
    }

    String getConnectionURL(){
        this.checkInitialization();
        return this.databaseConnectionPresenter.getConnectionURL();
    }

    DatabaseConnection getConnection() {
        try {
            return this.databaseConnectionUseCase.getConnection();
        } catch (DatabaseException e) {
            throw new DatabaseInteractionException(e);
        }
    }

    private void checkInitialization() {
        if(!initialized) {
            throw new IllegalStateException();
        }
    }

    private void setUp() {
        if (this.initialStatements == null) {
            throw new IllegalStateException();
        }
        if (!this.initialized) {
            this.initialize();
        }
    }

    private void initialize() {
        try {
            this.initUseCases();

            this.databaseConnectionUseCase.initDatabase(this.initialStatements);
            this.initialized = true;
        } catch (DatabaseException e) {
            throw new InitializationException(e);
        }
    }

    private void initUseCases(){
        this.databaseConnectionUseCase = new DatabaseConnectionInteractor(
                databaseConnectionPresenter,
                database,
                new GenericDatabaseCredentialGenerator()
        );
        this.comparisonUseCase =  new ComparisonContentInteractor(
                database,
                this.tableParser,
                new GenericTableBuilderFactory()
        );
        this.databaseContentUseCase.setDatabase(this.database);
        this.databaseContentUseCase.setTableParser(this.tableParser);
    }

    private String formatErrorMessage(Collection<ContentDifference> differences) {
        StringBuilder message = new StringBuilder();
        for (ContentDifference difference : differences) {
            message.append("Difference in table " + difference.getTableName());
            message.append(" with " + difference.getAttribute() + ".");
            message.append(" One value is " + difference.getValueOne() + ",");
            message.append(" other value is " + difference.getValueTwo());
            message.append(System.lineSeparator());
        }
        return message.toString();
    }

}
