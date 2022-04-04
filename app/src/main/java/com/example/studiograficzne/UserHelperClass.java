package com.example.studiograficzne;

public class UserHelperClass {

    String login, email, haslo;

    //pusty konstruktor dla firebase'a
    public UserHelperClass() {
    }

    //konstruktory
    public UserHelperClass(String login, String email, String haslo) {
        this.login = login;
        this.email = email;
        this.haslo = haslo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
}
