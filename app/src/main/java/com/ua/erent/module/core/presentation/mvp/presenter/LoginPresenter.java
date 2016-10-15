package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.presentation.mvp.model.ILoginModel;
import com.ua.erent.module.core.presentation.mvp.view.LoginActivity;
import com.ua.erent.module.core.util.IRetrieveCallback;

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
    public LoginPresenter(Context context, ILoginModel model) {
        this.model = model;
        this.context = context;
    }

    @Override
    public void onLogin(String login, String password) {
        model.login(new Credentials(login, password), new IRetrieveCallback<Session>() {

            @Override
            public void onResult(Session result) {
                Toast.makeText(context, "response " + result.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(@NotNull Exception exc) {
                Toast.makeText(context, "err " + exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
