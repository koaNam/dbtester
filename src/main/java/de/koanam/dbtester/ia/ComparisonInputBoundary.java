package de.koanam.dbtester.ia;

import de.koanam.dbtester.core.entity.ContentDifference;
import de.koanam.dbtester.framework.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface ComparisonInputBoundary {

    Collection<ContentDifference> compare(InputStream otherDataset) throws DatabaseException, IOException;

}
