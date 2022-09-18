package de.koanam.dbtester.core.entity;

public interface ContentDifference {

    String getTableName();

    String getAttribute();

    String getValueOne();

    String getValueTwo();

}
