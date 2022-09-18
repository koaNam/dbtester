package de.koanam.dbtester.ia;

import de.koanam.dbtester.core.entity.ContentDifference;

import java.io.InputStream;
import java.util.Collection;

public interface ComparisonInputBoundary {

    Collection<ContentDifference> compare(InputStream otherDataset);

}
