package de.koanam.dbtester.ia;

import java.io.InputStream;

public interface DatabaseContentInputBoundary {

    void insertContent(InputStream input);

    void clearContent(InputStream input);

}
