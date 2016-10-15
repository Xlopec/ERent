package com.ua.erent.module.core.account.auth.dto;

/**
 * Created by Максим on 10/14/2016.
 */

public final class Credentials {

    private final String login;
    private final String password;

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
