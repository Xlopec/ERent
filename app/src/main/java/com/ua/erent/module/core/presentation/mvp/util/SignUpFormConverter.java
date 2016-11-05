package com.ua.erent.module.core.presentation.mvp.util;

import android.content.Context;

import com.ua.erent.module.core.account.auth.vo.SignUpCredentials;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/29/2016.
 */
// move in another place
public final class SignUpFormConverter {

    private SignUpFormConverter() {
    }

    public static SignUpCredentials convert(@NotNull Context context, @NotNull SignUpForm form) {

        Preconditions.checkNotNull(form);

        final File avatarFile = form.getAvatarUri() == null ? null : new File(form.getAvatarUri().getPath());
        return new SignUpCredentials.Builder()
                .setAvatarImage(avatarFile)
                .setEmail(form.getEmail())
                .setUsername(form.getUsername())
                .setPassword(form.getPassword())
                .setConfPassword(form.getConfPassword())
                .build();
    }

}
