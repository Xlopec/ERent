package com.ua.erent.module.core.account.auth.vo;

import dagger.internal.Preconditions;

/**
 * <p>
 * POJO which is used to pass credentials through app components
 * </p>
 * Created by Максим on 10/14/2016.
 */

public final class SignInCredentials {

    private final String login;
    private final String password;

    public SignInCredentials(String login, String password) {
        this.login = Preconditions.checkNotNull(login);
        this.password = Preconditions.checkNotNull(password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignInCredentials that = (SignInCredentials) o;

        if (!login.equals(that.login)) return false;
        return password.equals(that.password);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SignInCredentials{" +
                "signIn='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
