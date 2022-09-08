package de.koanam.dbtester.core.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface TableParser {

    Collection<TableObject> parseTables(InputStream input) throws IOException;

}
