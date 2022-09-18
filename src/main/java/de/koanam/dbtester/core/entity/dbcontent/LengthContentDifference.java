package de.koanam.dbtester.core.entity.dbcontent;

import de.koanam.dbtester.core.entity.ContentDifference;

import java.util.Objects;

public class LengthContentDifference implements ContentDifference {

    private String tableName;
    private int leftValue;
    private int rightValue;

    public LengthContentDifference(String tableName, int leftValue, int rightValue) {
        this.tableName = tableName;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String getAttribute() {
        return "length";
    }

    @Override
    public String getValueOne() {
        return String.valueOf(this.leftValue);
    }

    @Override
    public String getValueTwo() {
        return String.valueOf(this.rightValue);
    }

    @Override
    public String toString() {
        return "LengthContentDifference{" +
                "tableName='" + tableName + '\'' +
                ", leftValue=" + leftValue +
                ", rightValue=" + rightValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LengthContentDifference that = (LengthContentDifference) o;

        if (leftValue != that.leftValue) return false;
        if (rightValue != that.rightValue) return false;
        return Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + leftValue;
        result = 31 * result + rightValue;
        return result;
    }
}
