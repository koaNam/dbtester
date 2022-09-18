package de.koanam.dbtester.core.entity.dbcontent;

import com.google.common.collect.Streams;
import de.koanam.dbtester.core.entity.ContentDifference;
import de.koanam.dbtester.core.entity.DatabaseContentComparator;
import de.koanam.dbtester.core.entity.TableObject;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TableIntersectionDatabaseContentComparator implements DatabaseContentComparator {

    public Collection<ContentDifference> compare(Collection<TableObject> left, Collection<TableObject> right){
        List<TableObject> leftTables = sortAndRemoveDifference(left, right);
        List<TableObject> rightTables = sortAndRemoveDifference(right, left);

        List<ContentDifference> differences =  Streams.zip(leftTables.stream(), rightTables.stream(), (l,r) -> l.getDifferences(r)).flatMap(List::stream).toList();
        return differences;
    }

    private List<TableObject> sortAndRemoveDifference(Collection<TableObject> baseContent, Collection<TableObject> differentContent) {
        return  baseContent.stream()
                .sorted(Comparator.comparing(TableObject::getTableName))
                .filter(o -> differentContent.stream().map(o2 -> o2.getTableName()).toList().contains(o.getTableName()))
                .toList();
    }


}
