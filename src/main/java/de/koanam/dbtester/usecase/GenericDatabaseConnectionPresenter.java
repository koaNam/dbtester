package de.koanam.dbtester.usecase;

public class GenericDatabaseConnectionPresenter implements DatabaseConnectionPresenter{

    private String username;
    private String password;
    private String connectionURL;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getConnectionURL() {
        return this.connectionURL;
    }

    @Override
    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }
}
