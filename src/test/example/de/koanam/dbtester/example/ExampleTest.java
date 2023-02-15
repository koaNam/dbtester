package de.koanam.dbtester.example;

import de.koanam.dbtester.framework.junit.DBTestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExampleTest extends DBTestCase {

    @BeforeAll
    static void setUp() throws IOException {
        DBTestCase.setPathToDDLs(Paths.get("./src/test/ddl.sql"));
    }

    @Test
    public void test1() throws IOException {
        this.setInitialDataset(Paths.get("./src/test/dataset1.md"));
        String statement = "UPDATE TABLE1 SET TEST2 = 'value22_diff' WHERE ID = 2";
        try(Connection con = this.getConnection(); PreparedStatement stmt =  con.prepareStatement(statement)){
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.assertEqualDataset(Paths.get("./src/test/dataset2.md"));
    }

    @Test
    public void test2() throws IOException {
        this.setInitialDataset(Paths.get("./src/test/dataset2.md"));
        this.assertEqualDataset(Paths.get("./src/test/dataset1.md"));
    }

    @Test
    public void test3() throws IOException {
        this.setInitialDataset(Paths.get("./src/test/dataset1.md"));
        this.assertEqualDataset(Paths.get("./src/test/dataset1.md"));
    }

}
