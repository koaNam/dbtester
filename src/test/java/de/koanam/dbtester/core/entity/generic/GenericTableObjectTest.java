package de.koanam.dbtester.core.entity.generic;

import de.koanam.dbtester.core.entity.ContentDifference;
import de.koanam.dbtester.core.entity.dbcontent.GenericContentDifference;
import de.koanam.dbtester.core.entity.dbcontent.LengthContentDifference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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

    @Test
    public void getDifferencesSameTables(){
        GenericTableObject table1 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        Collection<ContentDifference> actual = table1.getDifferences(table1);
        Assertions.assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void getDifferencesSingleAdditionalValue(){
        GenericTableObject table1 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        GenericTableObject table2 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .addRow(Arrays.asList("value31", "value32"))
                .build();

        Collection<ContentDifference> actual = table1.getDifferences(table2);

        LengthContentDifference expected = new LengthContentDifference("TABLE1", 2,3);
        Assertions.assertEquals(Arrays.asList(expected), actual);
    }

    @Test
    public void getDifferencesDifferentValue(){
        GenericTableObject table1 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        GenericTableObject table2 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22_diff"))
                .build();

        Collection<ContentDifference> actual = table1.getDifferences(table2);

        GenericContentDifference expected = new GenericContentDifference("TABLE1", "test2", "value22", "value22_diff");
        Assertions.assertEquals(Arrays.asList(expected), actual);
    }

    @Test
    public void getDifferencesDifferentValueInBeginning(){
        GenericTableObject table1 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11_diff", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        GenericTableObject table2 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        Collection<ContentDifference> actual = table1.getDifferences(table2);

        GenericContentDifference expected = new GenericContentDifference("TABLE1", "test1", "value11_diff", "value11");
        Assertions.assertEquals(Arrays.asList(expected), actual);
    }

}