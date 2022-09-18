package de.koanam.dbtester.core.entity;

import java.util.Collection;

public interface DatabaseContentComparator {

    Collection<ContentDifference> compare(Collection<TableObject> left, Collection<TableObject> right);

}
