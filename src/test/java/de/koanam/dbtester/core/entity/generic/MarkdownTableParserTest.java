package de.koanam.dbtester.core.entity.generic;

import de.koanam.dbtester.core.entity.TableBuilder;
import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class MarkdownTableParserTest {

    @Test
    void parseTable() throws IOException {
        String input = """
                #### TABLE1
                |test1|test2|
                |-----|-----|
                |value11|value12|
                |value21|value22|
                """;

        TableBuilderFactory tableBuilder = new GenericTableBuilderFactory();
        MarkdownTableParser parser = new MarkdownTableParser(tableBuilder);

        Collection<TableObject> actual = parser.parseTables(new ByteArrayInputStream(input.getBytes()));

        GenericTableBuilder expected = new GenericTableBuilder();
        expected.setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"));

        Assertions.assertEquals(Arrays.asList(expected.build()), actual);
    }

    @Test
    void parseTableWithThreeRows() throws IOException {
        String input = """
                #### TABLE1
                |test1|test2|
                |-----|-----|
                |value11|value12|
                |value21|value22|
                |value31|value32|
                """;

        TableBuilderFactory tableBuilder = new GenericTableBuilderFactory();
        MarkdownTableParser parser = new MarkdownTableParser(tableBuilder);

        Collection<TableObject> actual = parser.parseTables(new ByteArrayInputStream(input.getBytes()));

        GenericTableBuilder expected = new GenericTableBuilder();
        expected.setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"))
                .addRow(Arrays.asList("value31", "value32"));

        Assertions.assertEquals(Arrays.asList(expected.build()), actual);
    }

    @Test
    void parseMultipleTables() throws IOException {
        String input = """
                #### TABLE1
                |test1|test2|
                |-----|-----|
                |value11|value12|
                |value21|value22|
                ab 
                xy 
                 #### TABLE2
                |test3|test4|
                |-----|-----|
                |value31|value32|
                |value41|value42|
                """;

        TableBuilderFactory tableBuilder = new GenericTableBuilderFactory();
        MarkdownTableParser parser = new MarkdownTableParser(tableBuilder);

        Collection<TableObject> actual = parser.parseTables(new ByteArrayInputStream(input.getBytes()));

        List<GenericTableObject> expected = new ArrayList<>();

        GenericTableBuilder expectedBuilder = new GenericTableBuilder();
        expectedBuilder.setName("TABLE1")
                .setColumnNames(Arrays.asList("test1", "test2"))
                .addRow(Arrays.asList("value11", "value12"))
                .addRow(Arrays.asList("value21", "value22"));
        expected.add(expectedBuilder.build());

        expectedBuilder = new GenericTableBuilder();
        expectedBuilder.setName("TABLE2")
                .setColumnNames(Arrays.asList("test3", "test4"))
                .addRow(Arrays.asList("value31", "value32"))
                .addRow(Arrays.asList("value41", "value42"));
        expected.add(expectedBuilder.build());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void parseNoTables() throws IOException {
        String input = """
                abcxyz
                """;

        TableBuilderFactory tableBuilder = new GenericTableBuilderFactory();
        MarkdownTableParser parser = new MarkdownTableParser(tableBuilder);

        Collection<TableObject> actual = parser.parseTables(new ByteArrayInputStream(input.getBytes()));

        List<GenericTableObject> expected = new ArrayList<>();
        Assertions.assertEquals(expected, actual);
    }
}