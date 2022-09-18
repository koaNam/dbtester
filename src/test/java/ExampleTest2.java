import de.koanam.dbtester.framework.junit.DBTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class ExampleTest2 extends DBTestCase {

    @BeforeAll
    static void setUp() throws IOException {
        DBTestCase.setPathToDDLs(Paths.get("./src/test/ddl.sql"));
        System.out.println("BeforeAll-Methode");
    }

    @Test
    public void test1(){
        Assertions.assertEquals(1, 2);
    }

    @Test
    public void test2(){
        Assertions.assertEquals(2, 2);
    }

    @Test
    public void test3(){
        Assertions.assertEquals(3, 3);
    }

}
