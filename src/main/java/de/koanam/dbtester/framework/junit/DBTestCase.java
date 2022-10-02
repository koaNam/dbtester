package de.koanam.dbtester.framework.junit;

import de.koanam.dbtester.framework.h2.JDBCDatabaseConnection;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class DBTestCase {

    @RegisterExtension
    static DbTestExtension dbTestExtension = new DbTestExtension();

    static protected void setPathToDDLs(Path path) throws IOException {
        String fileContent = Files.readString(path);
        List<String> statements = Arrays.asList(fileContent.split(";"));

        dbTestExtension.setInitialStatements(statements);
    }

    protected void setInitialDataset(Path path) throws IOException {
        String datasetContent = Files.readString(path);

        dbTestExtension.setCurrentInitialDatasetContent(datasetContent);
        dbTestExtension.insertContent();
    }

    protected void assertEqualDataset(Path expectedDataset) throws IOException {
        String datasetContent = Files.readString(expectedDataset);

        dbTestExtension.assertEqualDataset(datasetContent);
    }

    protected Connection getConnection(){
        JDBCDatabaseConnection connection = (JDBCDatabaseConnection) dbTestExtension.getConnection();
        return connection.getConnection();
    }

    protected String getConnectionURL(){
        return dbTestExtension.getConnectionURL();
    }

    protected String getUser(){
        return dbTestExtension.getUser();
    }

    protected String getPassword(){
        return dbTestExtension.getPassword();
    }

}
