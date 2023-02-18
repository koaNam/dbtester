package de.koanam.dbtester.framework.junit;

import de.koanam.dbtester.usecase.ClearingDatabaseContentInteractor;
import de.koanam.dbtester.usecase.DatabaseContentInteractor;

public enum PostTestAction {
    NONE(new DatabaseContentInteractor()),
    CLEANUP(new ClearingDatabaseContentInteractor());

    private DatabaseContentInteractor databaseContentInteractor;

    PostTestAction(DatabaseContentInteractor databaseContentInteractor) {
        this.databaseContentInteractor = databaseContentInteractor;
    }

    public DatabaseContentInteractor getDatabaseContentInteractor() {
        return databaseContentInteractor;
    }
}
