package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import android.content.Context;
import android.content.Intent;

import com.ua.erent.module.core.init.IInitCallback;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/15/2016.
 */

public interface ILoginModel {

    Intent createLoginIntent(@NotNull Context context);

    boolean isSessionAlive();

    String getLastLogin();

    void login(@NotNull IInitCallback callback);

    void login(String login, String password, IInitCallback callback);

}
