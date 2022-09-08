package de.koanam.dbtester.core.entity.generic;

import de.koanam.dbtester.core.entity.TableBuilder;
import de.koanam.dbtester.core.entity.UnfittingTableFormatException;

import java.util.ArrayList;
import java.util.List;

public class GenericTableBuilder implements TableBuilder {

    private String tableName;

    private List<String> columnNames;
    private List<List<String>> values = new ArrayList<>();

    @Override
    public GenericTableBuilder setName(String tableName){
        this.tableName = tableName;
        return this;
    }

    @Override
    public GenericTableBuilder setColumnNames(List<String> columnNames) throws UnfittingTableFormatException {
        if(!this.values.isEmpty() && this.values.get(0).size() != columnNames.size()){
            throw new UnfittingTableFormatException("Width of column names does not match already existing rows", this.values.get(0).size(), columnNames.size());
        }
        this.columnNames = columnNames;
        return this;
    }

    @Override
    public GenericTableBuilder addRow(List<String> row) throws UnfittingTableFormatException {
        if(columnNames == null){
            this.initColumnNameMap(row);
        }
        if(row.size() != this.columnNames.size()){
            throw new UnfittingTableFormatException("Width of new row does not match existing table width", this.columnNames.size(), row.size());
        }

        this.values.add(row);
        return this;
    }

    @Override
    public GenericTableObject build() {
        GenericTableObject tableObject = new GenericTableObject(this.tableName);
        for(List<String> row: values){
            tableObject.addValues(this.columnNames, row);
        }
        return tableObject;
    }

    private void initColumnNameMap(List<String> row){
        this.columnNames = new ArrayList<>(row.size());
    }


}
