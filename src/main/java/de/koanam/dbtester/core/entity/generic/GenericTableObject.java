package de.koanam.dbtester.core.entity.generic;

import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Streams;
import com.google.common.collect.Table;
import de.koanam.dbtester.core.entity.TableObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
