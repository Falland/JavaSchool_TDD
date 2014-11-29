package com.db.javaschool.tdd;

public interface IAccount {

    public boolean passwordMatches(String pass);

    public boolean loggedIn();

    public void setLoggedIn();

    public void setRevoked();

    public boolean isRevoked();

}
