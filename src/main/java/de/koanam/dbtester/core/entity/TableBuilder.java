package de.koanam.dbtester.core.entity;

import java.util.List;

public interface TableBuilder {

    TableBuilder setName(String tableName) throws UnfittingTableFormatException;

    TableBuilder setColumnNames(List<String> columnNames)  throws UnfittingTableFormatException;

    TableBuilder addRow(List<String> row)  throws UnfittingTableFormatException;

    TableObject build();
}
