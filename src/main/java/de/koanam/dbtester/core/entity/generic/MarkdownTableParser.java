package de.koanam.dbtester.core.entity.generic;

import de.koanam.dbtester.core.entity.TableBuilder;
import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.TableParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownTableParser implements TableParser {

    private static final Pattern MARKDOWN_TABLE_REGEX = Pattern.compile("#+ *([\\S ]+) *[\\r\\n]+ *\\|([\\S ]+)\\| *[\\r\\n]+ *\\|[\\-|]{3,}+ *[\\r\\n]+ *((\\|([\\S ]+)\\| *[\\r\\n]+ *)*\\|([\\S |]+)\\|) *[\\r\\n]{0,1}");
    private static final Pattern MARKDOWN_ROW_REGEX = Pattern.compile("\\|([\\S ]+)\\|");


    public static final String UTF_8_CHARSET = "UTF-8";


    private TableBuilderFactory tableBuilderFactory;

    public MarkdownTableParser(TableBuilderFactory tableBuilderFactory){
        this.tableBuilderFactory = tableBuilderFactory;
    }

    @Override
    public Collection<TableObject> parseTables(InputStream input) throws IOException {

        String inputString = this.inputStreamToString(input);
        Matcher matcher = MARKDOWN_TABLE_REGEX.matcher(inputString);


        List<TableObject> tables = new ArrayList<>();
        while(matcher.find()){
            TableBuilder tableBuilder = this.tableBuilderFactory.getBuilder();

            this.buildTable(matcher, tableBuilder);
            TableObject table = tableBuilder.build();
            tables.add(table);
        }

        return tables;
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        String inputString = new String(bytes, UTF_8_CHARSET);
        return inputString;
    }

    private void buildTable(Matcher matcher, TableBuilder tableBuilder) {
        String tableName = matcher.group(1);
        tableBuilder.setName(tableName);

        List<String> tableHeader = this.splitMarkdownRow(matcher, 2);
        tableBuilder.setColumnNames(tableHeader);

        String rows = matcher.group(3);
        this.processTableRows(rows, tableBuilder);
    }

    private void processTableRows(String rowString, TableBuilder tableBuilder){
        Matcher matcher = MARKDOWN_ROW_REGEX.matcher(rowString);
        while (matcher.find()){
            List<String> rows = this.splitMarkdownRow(matcher, 1);
            tableBuilder.addRow(rows);
        }
    }

    private List<String> splitMarkdownRow(Matcher matcher, int groupIndex) {
        String row = matcher.group(groupIndex);
        String[] headerValues = row.split("\\|");
        return Arrays.asList(headerValues);
    }

}
