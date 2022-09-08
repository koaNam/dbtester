package de.koanam.dbtester.core.entity.generic;

import de.koanam.dbtester.core.entity.TableBuilder;
import de.koanam.dbtester.core.entity.TableBuilderFactory;

public class GenericTableBuilderFactory implements TableBuilderFactory {

    @Override
    public TableBuilder getBuilder() {
        return new GenericTableBuilder();
    }
}
