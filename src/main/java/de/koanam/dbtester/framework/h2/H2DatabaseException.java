package de.koanam.dbtester.framework.h2;

public class H2DatabaseException extends Exception{

    public H2DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
