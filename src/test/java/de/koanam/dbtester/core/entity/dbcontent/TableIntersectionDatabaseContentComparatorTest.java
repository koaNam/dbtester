package de.koanam.dbtester.core.entity.dbcontent;

import de.koanam.dbtester.core.entity.ContentDifference;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.generic.GenericTableBuilder;
import de.koanam.dbtester.core.entity.generic.GenericTableObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TableIntersectionDatabaseContentComparatorTest {

    private TableIntersectionDatabaseContentComparator comparator = new TableIntersectionDatabaseContentComparator();

    @Test
    public void compareSame() {
        GenericTableObject table1 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        GenericTableObject table2 = new GenericTableBuilder()
                .setName("TABLE2")
                .setColumnNames(Arrays.asList("test3", "test4"))
                .addRow(Arrays.asList("value13", "value14"))
                .addRow(Arrays.asList("value23", "value24"))
                .build();

        List<TableObject> tables = Arrays.asList(table1, table2);

        Collection<ContentDifference> actual = comparator.compare(tables, tables);
        Assertions.assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void compareSingleDifferentValueSecondTable() {
        GenericTableObject table1 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        GenericTableObject table2 = new GenericTableBuilder()
                .setName("TABLE2")
                .setColumnNames(Arrays.asList("test3", "test4"))
                .addRow(Arrays.asList("value13", "value14"))
                .addRow(Arrays.asList("value23", "value24"))
                .build();

        GenericTableObject table2Difference = new GenericTableBuilder()
                .setName("TABLE2")
                .setColumnNames(Arrays.asList("test3", "test4"))
                .addRow(Arrays.asList("value13", "value14"))
                .addRow(Arrays.asList("value23", "value24_diff"))
                .build();

        List<TableObject> tables = Arrays.asList(table1, table2);
        List<TableObject> tablesDifference = Arrays.asList(table1, table2Difference);

        Collection<ContentDifference> actual = comparator.compare(tablesDifference, tables);

        GenericContentDifference difference = new GenericContentDifference("TABLE2", "TEST4", "value24_diff", "value24");
        Assertions.assertEquals(Collections.singletonList(difference), actual);
    }

    @Test
    public void compareSingleDifferentValueFirstTable() {
        GenericTableObject table1 = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        GenericTableObject table1Difference = new GenericTableBuilder()
                .setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11_diff", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .build();

        GenericTableObject table2 = new GenericTableBuilder()
                .setName("TABLE2")
                .setColumnNames(Arrays.asList("test3", "test4"))
                .addRow(Arrays.asList("value13", "value14"))
                .addRow(Arrays.asList("value23", "value24"))
                .build();


        List<TableObject> tables = Arrays.asList(table1, table2);
        List<TableObject> tablesDifference = Arrays.asList(table1Difference, table2);

        Collection<ContentDifference> actual = comparator.compare(tables, tablesDifference);

        GenericContentDifference difference = new GenericContentDifference("TABLE1", "TEST1", "value11", "value11_diff");
        Assertions.assertEquals(Collections.singletonList(difference), actual);
    }

}