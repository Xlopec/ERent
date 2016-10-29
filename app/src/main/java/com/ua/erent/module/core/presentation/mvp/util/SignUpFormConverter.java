package com.ua.erent.module.core.presentation.mvp.util;

import android.content.Context;

import com.ua.erent.R;
import com.ua.erent.module.core.account.auth.vo.SignUpCredentials;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/29/2016.
 */
// move in another place
public final class SignUpFormConverter {

    private SignUpFormConverter() {}

    public static SignUpCredentials convert(@NotNull Context context, @NotNull SignUpForm form) {

        Preconditions.checkNotNull(form);

        final RegexValidator usernameValid =
                new RegexValidator(context.getString(R.string.regex_username), true);
        final EmailValidator emailValidator = EmailValidator.getInstance();
        final RegexValidator passwordValidator =
                new RegexValidator(context.getString(R.string.regex_password), true);

        if(!usernameValid.isValid(form.getUsername()))
            throw new IllegalArgumentException();

        if(!emailValidator.isValid(form.getEmail()))
            throw new IllegalArgumentException();

        if(!passwordValidator.isValid(form.getPassword()) ||
                !form.getPassword().equals(form.getConfPassword()))
            throw new IllegalArgumentException();

        return new SignUpCredentials.Builder()
                .setEmail(form.getEmail())
                .setUsername(form.getUsername())
                .setPassword(form.getPassword())
                .setConfPassword(form.getConfPassword())
                .build();
    }

}
