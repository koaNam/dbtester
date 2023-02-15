package de.koanam.dbtester.example;

import de.koanam.dbtester.framework.junit.DBTestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class ExampleTest2 extends DBTestCase {

    @BeforeAll
    static void setUp() throws IOException {
        DBTestCase.setPathToDDLs(Paths.get("./src/test/ddl.sql"));
    }

    @Test
    public void test2() throws IOException {
        this.setInitialDataset(Paths.get("./src/test/dataset2.md"));
        this.assertEqualDataset(Paths.get("./src/test/dataset2.md"));
    }

}
