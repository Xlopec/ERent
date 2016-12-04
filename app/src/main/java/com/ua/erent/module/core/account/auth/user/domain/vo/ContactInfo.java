package com.ua.erent.module.core.account.auth.user.domain.vo;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/4/2016.
 */

public final class ContactInfo {

    public static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private final String email;
    private final String phone;
    private final String skype;

    public ContactInfo(@NotNull String email, String phone, String skype) {

        if(!EMAIL_VALIDATOR.isValid(email))
            throw new IllegalArgumentException(
                    String.format("specified email %s is invalid", email));

        this.email = email;
        this.phone = phone;
        this.skype = skype;
    }

    public String getSkype() {
        return skype;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", skype='" + skype + '\'' +
                '}';
    }
}
