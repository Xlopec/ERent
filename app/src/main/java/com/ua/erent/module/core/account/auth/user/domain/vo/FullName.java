package com.ua.erent.module.core.account.auth.user.domain.vo;

import com.ua.erent.module.core.util.validation.Regexes;

import org.apache.commons.validator.routines.RegexValidator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/4/2016.
 */

public final class FullName {

    private static final RegexValidator USERNAME_VALIDATOR = new RegexValidator(Regexes.USERNAME);

    private final String username;

    public FullName(@NotNull String username) {

        if(!USERNAME_VALIDATOR.isValid(username))
            throw new IllegalArgumentException(String.format("invalid username, was %s", username));

        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
