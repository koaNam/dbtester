package de.koanam.dbtester.ia;

import java.io.InputStream;

public interface ComparisonInputBoundary {

    boolean compare(InputStream otherDataset);

}
