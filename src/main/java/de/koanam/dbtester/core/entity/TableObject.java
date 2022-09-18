package de.koanam.dbtester.core.entity;

import java.util.List;
import java.util.Map;

public interface TableObject {

    List<Map<String, String>> getContent();

    String getTableName();

    List<ContentDifference> getDifferences(TableObject other);

}
