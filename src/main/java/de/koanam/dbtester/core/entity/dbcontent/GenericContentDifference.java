package de.koanam.dbtester.core.entity.dbcontent;

import de.koanam.dbtester.core.entity.ContentDifference;

import java.util.Objects;

public class GenericContentDifference implements ContentDifference {

    private String tableName;
    private String columnName;
    private String leftValue;
    private String rightValue;

    public GenericContentDifference(String tableName, String columnName, String leftValue, String rightValue) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String getAttribute() {
        return this.columnName;
    }

    @Override
    public String getValueOne() {
        return this.leftValue;
    }

    @Override
    public String getValueTwo() {
        return this.rightValue;
    }
    @Override
    public String toString() {
        return "GenericContentDifference{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", leftValue='" + leftValue + '\'' +
                ", rightValue='" + rightValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericContentDifference that = (GenericContentDifference) o;

        if (!Objects.equals(tableName, that.tableName)) return false;
        if (!Objects.equals(columnName, that.columnName)) return false;
        if (!Objects.equals(leftValue, that.leftValue)) return false;
        return Objects.equals(rightValue, that.rightValue);
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (leftValue != null ? leftValue.hashCode() : 0);
        result = 31 * result + (rightValue != null ? rightValue.hashCode() : 0);
        return result;
    }
}
