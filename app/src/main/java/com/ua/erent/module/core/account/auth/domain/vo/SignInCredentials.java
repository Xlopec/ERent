package com.ua.erent.module.core.account.auth.domain.vo;

import com.ua.erent.module.core.util.validation.Regexes;

import org.apache.commons.validator.routines.RegexValidator;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * POJO which is used to pass credentials through app components
 * </p>
 * Created by Максим on 10/14/2016.
 */

public final class SignInCredentials {

    private static final RegexValidator USERNAME_VALIDATOR = new RegexValidator(Regexes.SIGN_IN_USERNAME, true);
    private static final RegexValidator PASSWORD_VALIDATOR = new RegexValidator(Regexes.PASSWORD, true);

    private final String login;
    private final String password;

    public SignInCredentials(@NotNull String login, @NotNull String password) {

        if (!USERNAME_VALIDATOR.isValid(login))
            throw new IllegalArgumentException(String.format("login %s isn't valid", login));

        if (!PASSWORD_VALIDATOR.isValid(password))
            throw new IllegalArgumentException(String.format("password %s isn't valid", password));

        this.login = login;
        this.password = password;
    }

    public String getUsername() {
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
