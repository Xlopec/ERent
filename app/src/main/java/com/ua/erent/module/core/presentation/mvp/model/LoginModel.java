package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.content.Intent;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.account.auth.domain.vo.SignInCredentials;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ILoginModel;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public final class LoginModel implements ILoginModel {

    private final IAuthAppService authHandler;

    @Inject
    public LoginModel(IAuthAppService authHandler) {
        this.authHandler = authHandler;
    }

    @Override
    public Intent createLoginIntent(@NotNull Context context) {

        final Intent intent = new Intent(context, CategoriesActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }

    @Override
    public boolean isSessionAlive() {
        return authHandler.isSessionAlive();
    }

    @Override
    public String getLastLogin() {
        final Session session = authHandler.getSession();
        return session == null ? null : session.getUsername();
    }

    @Override
    public void login(@NotNull IInitCallback callback) {
        authHandler.login(callback);
    }

    @Override
    public void login(String login, String password, IInitCallback callback) {
        authHandler.login(new SignInCredentials(login, password), callback);
    }

}
