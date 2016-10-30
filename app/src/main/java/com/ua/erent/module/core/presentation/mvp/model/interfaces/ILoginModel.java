package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import com.ua.erent.module.core.account.auth.domain.ILoginCallback;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/15/2016.
 */

public interface ILoginModel {

    boolean isSessionAlive();

    String getLastLogin();

    void login(@NotNull ILoginCallback callback);

    void login(String login, String password, ILoginCallback callback);

}
