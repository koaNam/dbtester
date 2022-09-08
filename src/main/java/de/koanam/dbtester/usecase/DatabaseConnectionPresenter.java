package de.koanam.dbtester.usecase;

public interface DatabaseConnectionPresenter {

    String getUsername();
    void setUsername(String username);

    String getPassword();
    void setPassword(String password);

}
