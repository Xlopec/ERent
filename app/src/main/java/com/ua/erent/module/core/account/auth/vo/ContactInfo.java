package com.ua.erent.module.core.account.auth.vo;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/4/2016.
 */

public final class ContactInfo {

    public static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private final String email;

    public ContactInfo(@NotNull String email) {

        if(!EMAIL_VALIDATOR.isValid(email))
            throw new IllegalArgumentException(
                    String.format("specified email %s is invalid", email));

        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
