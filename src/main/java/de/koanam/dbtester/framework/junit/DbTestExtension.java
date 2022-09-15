package de.koanam.dbtester.framework.junit;

import de.koanam.dbtester.core.entity.generic.GenericDatabaseCredentialGenerator;
import de.koanam.dbtester.core.entity.generic.GenericTableBuilderFactory;
import de.koanam.dbtester.core.entity.generic.MarkdownTableParser;
import de.koanam.dbtester.framework.h2.H2Database;
import de.koanam.dbtester.ia.ComparisonInputBoundary;
import de.koanam.dbtester.ia.DatabaseConnectionInputBoundary;
import de.koanam.dbtester.ia.DatabaseContentInputBoundary;
import de.koanam.dbtester.usecase.*;
import org.junit.jupiter.api.extension.*;

import java.io.ByteArrayInputStream;
import java.util.List;

public class DbTestExtension implements AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private boolean initialized = false;
    private List<String> initialStatements;

    private String currentInitialDatasetContent;

    private H2Database db = new H2Database();

    private DatabaseConnectionInputBoundary databaseConnectionUseCase = new DatabaseConnectionInteractor(
            new GenericDatabaseConnectionPresenter(),
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
    public void afterAll(ExtensionContext extensionContext) {
        this.databaseConnectionUseCase.stopDatabase();
        this.initialized = false;
        this.initialStatements = null;
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        ByteArrayInputStream initialDataset = new ByteArrayInputStream(this.currentInitialDatasetContent.getBytes());
        this.databaseContentUseCase.clearContent(initialDataset);
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        this.setUp();
    }

    public void insertContent(){
        ByteArrayInputStream initialDataset = new ByteArrayInputStream(this.currentInitialDatasetContent.getBytes());
        this.databaseContentUseCase.insertContent(initialDataset);
    }
    public void setInitialStatements(List<String> initialStatements) {
        this.initialStatements = initialStatements;
    }

    public void setCurrentInitialDatasetContent(String currentInitialDatasetContent) {
        this.currentInitialDatasetContent = currentInitialDatasetContent;
    }

    public boolean assertEqualDataset(String expectedDatasetContent){
        ByteArrayInputStream expectedDataset = new ByteArrayInputStream(expectedDatasetContent.getBytes());
        return this.comparisonUseCase.compare(expectedDataset);
    }

    private void setUp(){
        if(this.initialStatements == null){
            throw new IllegalStateException();
        }
        if(!this.initialized){
            this.initialize();
        }
    }

    private void initialize(){
        this.databaseConnectionUseCase.initDatabase(this.initialStatements);
        this.initialized = true;
    }

}
