package de.koanam.dbtester.framework.junit;

import de.koanam.dbtester.core.entity.ContentDifference;
import de.koanam.dbtester.core.entity.generic.GenericDatabaseCredentialGenerator;
import de.koanam.dbtester.core.entity.generic.GenericTableBuilderFactory;
import de.koanam.dbtester.core.entity.generic.MarkdownTableParser;
import de.koanam.dbtester.framework.DatabaseException;
import de.koanam.dbtester.framework.DatabaseInteractionException;
import de.koanam.dbtester.framework.InitializationException;
import de.koanam.dbtester.framework.h2.H2Database;
import de.koanam.dbtester.ia.ComparisonInputBoundary;
import de.koanam.dbtester.ia.DatabaseConnection;
import de.koanam.dbtester.ia.DatabaseConnectionInputBoundary;
import de.koanam.dbtester.ia.DatabaseContentInputBoundary;
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

    private H2Database db = new H2Database();

    private DatabaseConnectionPresenter databaseConnectionPresenter = new GenericDatabaseConnectionPresenter();

    private DatabaseConnectionInputBoundary databaseConnectionUseCase = new DatabaseConnectionInteractor(
            databaseConnectionPresenter,
            db,
            new GenericDatabaseCredentialGenerator()
    );

    private DatabaseContentInputBoundary databaseContentUseCase = new ClearingDatabaseContentInteractor(
            db,
            new MarkdownTableParser(new GenericTableBuilderFactory())
    );

    private ComparisonInputBoundary comparisonUseCase = new ComparisonContentInteractor(
            db,
            new MarkdownTableParser(new GenericTableBuilderFactory()),
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

    public void insertContent() {
        try {
            ByteArrayInputStream initialDataset = new ByteArrayInputStream(this.currentInitialDatasetContent.getBytes());
            this.databaseContentUseCase.insertContent(initialDataset);
        } catch (DatabaseException | IOException e) {
            throw new DatabaseInteractionException(e);
        }
    }

    public void setInitialStatements(List<String> initialStatements) {
        this.initialStatements = initialStatements;
    }

    public void setCurrentInitialDatasetContent(String currentInitialDatasetContent) {
        this.currentInitialDatasetContent = currentInitialDatasetContent;
    }

    public void assertEqualDataset(String expectedDatasetContent) {
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

    public String getUser(){
        this.checkInitialization(!this.initialized);
        return databaseConnectionPresenter.getUsername();
    }

    public String getPassword(){
        this.checkInitialization(!this.initialized);
        return databaseConnectionPresenter.getPassword();
    }

    public String getConnectionURL(){
        this.checkInitialization(!this.initialized);
        return this.databaseConnectionPresenter.getConnectionURL();
    }

    public DatabaseConnection getConnection() {
        try {
            return this.databaseConnectionUseCase.getConnection();
        } catch (DatabaseException e) {
            throw new DatabaseInteractionException(e);
        }
    }

    private void checkInitialization(boolean initialized) {
        if(initialized) {
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
            this.databaseConnectionUseCase.initDatabase(this.initialStatements);
            this.initialized = true;
        } catch (DatabaseException e) {
            throw new InitializationException(e);
        }
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
