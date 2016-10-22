package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.account.auth.domain.session.ISessionStorage;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.presentation.mvp.model.ILoginModel;
import com.ua.erent.module.core.presentation.mvp.view.LoginActivity;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public final class LoginPresenter extends ILoginPresenter {

    private final ILoginModel model;
    private final Context context;

    @Inject
    public LoginPresenter(Context context, ILoginModel model, ISessionStorage sessionStorage) {
        this.model = model;
        this.context = context;
        int id = sessionStorage.hashCode();
        Log.i("TAG", "hash code " + id);
    }

    @Override
    public void onLogin(String login, String password) {
        model.login(new Credentials(login, password), new ILoginCallback() {
            @Override
            public void onPreExecute() {
                Log.i("tag", "initialized");
            }

            @Override
            public void onInitialized() {
                Log.i("tag", "initialized");
            }

            @Override
            public void onComponentInitialized(@NotNull Initializeable initializeable, int progress, int total) {
                Log.i("tag", "initialized");
            }

            @Override
            public void onException(@NotNull Initializeable initializeable, @NotNull Throwable th) {
                Log.i("tag", "initialized");
            }

            @Override
            public void onFailure(@NotNull Initializeable initializeable, @NotNull Throwable th) {
                Log.i("tag", "initialized");
            }

            @Override
            public void onFailure(@NotNull Throwable th) {
                Log.i("tag", "initialized");
            }
        });
       // getView().startActivity(new Intent(context, NextActivity.class));
      //  getView().finish();
    }

    @Override
    protected void onViewAttached(@NotNull LoginActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {
        Log.d("test", "test");
    }

    @Override
    protected void onDestroyed() {
        Log.d("test", "test");
    }
}
