package de.koanam.dbtester.ia;

public interface DatabaseConnection<T> {

    T getConnection();

}
