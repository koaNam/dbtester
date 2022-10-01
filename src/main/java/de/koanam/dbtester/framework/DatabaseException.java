package de.koanam.dbtester.framework;

public class DatabaseException extends Exception{

    public DatabaseException(Throwable cause) {
        super(cause);
    }
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
