package de.koanam.dbtester.core.entity.generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class GenericTableObjectTest {

    @Test
    public void addValues() {
        GenericTableObject table = new GenericTableObject("Test Table");
        table.addValues(Arrays.asList("Column1", "Column2"), Arrays.asList("Value11", "Value12"));
        table.addValues(Arrays.asList("Column1", "Column2"), Arrays.asList("Value21", "Value22"));

        String actualTableName = table.getTableName();
        String expectedTableName = "Test Table";


        Assertions.assertEquals(expectedTableName, actualTableName);
    }


}