package de.koanam.dbtester.core.entity.generic;

import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Streams;
import com.google.common.collect.Table;
import de.koanam.dbtester.core.entity.ContentDifference;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.dbcontent.GenericContentDifference;
import de.koanam.dbtester.core.entity.dbcontent.LengthContentDifference;

import java.util.*;
import java.util.stream.Collectors;

public class GenericTableObject implements TableObject {

    String tableName;

    Table<Long, String, String> values;

    GenericTableObject(String tableName) {
        this.tableName = tableName;
        this.values = HashBasedTable.create();
    }

    void addValues(List<String> columnNames, List<String> values) {
        long lastPosition = this.values.rowKeySet().size();
        Streams
                .zip(columnNames.stream(), values.stream(), (c, v) -> new String[]{c, v})
                .forEach(pair -> this.values.put(lastPosition, pair[0], pair[1]));
    }

    @Override
    public List<Map<String, String>> getContent() {
        Set<Long> keys = this.values.rowKeySet();
        return keys.stream().sorted().map(k -> this.values.row(k)).collect(Collectors.toList());
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public List<ContentDifference> getDifferences(TableObject other){
        List<Map<String, String>> otherContent = other.getContent();

        Map<String, String> row1 = this.getContent().get(0);
        Map<String, String> row2 = otherContent.get(0);
        List<String> differentColumns = row1.keySet().stream().filter(c -> !row2.keySet().contains(c)).toList();

        List<Map<String,String>> thisOrderedRows = this.getContent().stream().sorted(Comparator.comparing(v -> v.values().toString())).toList();
        List<Map<String,String>> otherOrderedRows = other.getContent().stream().sorted(Comparator.comparing(v -> v.values().toString())).toList();

        if(thisOrderedRows.size() != otherOrderedRows.size()){
            return Collections.singletonList(new LengthContentDifference(this.tableName, thisOrderedRows.size(), otherOrderedRows.size()));
        }

        List<ContentDifference> differences = new ArrayList<>();
        for(int i=0; i < thisOrderedRows.size(); i++){
            differences.addAll(this.getDifference(thisOrderedRows.get(i), otherOrderedRows.get(i)));
        }

        return differences;
    }

    private List<ContentDifference> getDifference(Map<String, String> row1, Map<String, String> row2) {
        List<ContentDifference> differences = new ArrayList<>();

        Set<String> columns = row1.keySet();
        for(String column: columns){
            String value1 = row1.get(column);
            String value2 = row2.get(column);

            if(!value1.equals(value2)){
                differences.add(new GenericContentDifference(this.tableName, column, value1, value2));
            }
        }

        return differences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericTableObject that = (GenericTableObject) o;
        return Objects.equal(tableName, that.tableName) &&
                Objects.equal(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableName, values);
    }

    @Override
    public String toString() {
        return "GenericTableObject{" +
                "tableName='" + tableName + '\'' +
                ", values=" + values +
                '}';
    }

}
