package com.ua.erent.module.core.presentation.mvp.util;

import com.ua.erent.module.core.account.auth.domain.vo.SignUpCredentials;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/29/2016.
 */
// move in another place
public final class SignUpFormConverter {

    private SignUpFormConverter() {
    }

    public static SignUpCredentials convert(@NotNull SignUpForm form) {

        Preconditions.checkNotNull(form);

        return new SignUpCredentials.Builder()
                .setEmail(form.getEmail())
                .setUsername(form.getUsername())
                .setPassword(form.getPassword())
                .setConfPassword(form.getConfPassword())
                .build();
    }

}
