package de.koanam.dbtester.framework.junit;

import de.koanam.dbtester.core.entity.generic.GenericTableBuilderFactory;
import de.koanam.dbtester.core.entity.generic.MarkdownTableParser;
import de.koanam.dbtester.core.entity.generic.XMLTableParser;

public enum TableParser {

    MARKDOWN(new MarkdownTableParser(new GenericTableBuilderFactory())),
    XML(new XMLTableParser(new GenericTableBuilderFactory()));

    private de.koanam.dbtester.core.entity.TableParser tableParser;

    TableParser(de.koanam.dbtester.core.entity.TableParser tableParser) {
        this.tableParser = tableParser;
    }

    public de.koanam.dbtester.core.entity.TableParser getTableParser() {
        return tableParser;
    }
}
