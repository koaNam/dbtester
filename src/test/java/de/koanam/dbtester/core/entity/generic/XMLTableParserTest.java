package de.koanam.dbtester.core.entity.generic;

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

class XMLTableParserTest {

    @Test
    void parseTable() throws IOException {
        String input = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <dataset>
                              <table name="TABLE1">
                                  <column>test1</column>
                                  <column>test2</column>
                                  <row>
                                      <value>value11</value>
                                      <value>value12</value>
                                  </row>
                                  <row>
                                      <value>value21</value>
                                      <value>value22</value>
                                  </row>
                              </table>
                              <table name="TABLE2">
                                  <column>test3</column>
                                  <column>test4</column>
                                  <row>
                                      <value>value31</value>
                                      <value>value32</value>
                                  </row>
                                  <row>
                                      <value>value41</value>
                                      <value>value42</value>
                                  </row>
                              </table>
                              <table name='EMPTY_TABLE'>
                                  <column>COLUMN0</column>
                                  <column>COLUMN1</column>
                              </table>
                          </dataset>
                """;

        TableBuilderFactory tableBuilder = new GenericTableBuilderFactory();
        XMLTableParser parser = new XMLTableParser(tableBuilder);
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

        expectedBuilder = new GenericTableBuilder();
        expectedBuilder.setName("EMPTY_TABLE")
                .setColumnNames(Arrays.asList("COLUMN0", "COLUMN1"));
        expected.add(expectedBuilder.build());

        Assertions.assertEquals(expected, actual);
    }

}