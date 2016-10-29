package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.presentation.mvp.model.ILoginModel;
import com.ua.erent.module.core.presentation.mvp.view.IInitialScreenView;
import com.ua.erent.module.core.presentation.mvp.view.LoginFragment;
import com.ua.erent.module.core.presentation.mvp.view.MainActivity;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Максим on 10/15/2016.
 */

public final class LoginPresenter extends ILoginPresenter {

    private final ILoginModel model;
    private IInitialScreenView.NavigationListener callback;
    private final Context context;

    private class LoginCallbackImp implements ILoginCallback {

        private final Subscriber<? super String> subscriber;

        public LoginCallbackImp(Subscriber<? super String> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onPreExecute() {
            subscriber.onNext("Loading...");
            getView().showProgressView();
        }

        @Override
        public void onInitialized() {
            subscriber.onNext("Loaded...");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                final Intent intent = new Intent(getView().getActivity(), MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getView().hideProgressView();
                getView().startActivity(intent);
                getView().getActivity().finish();
            }, 2000L);
        }

        @Override
        public void onComponentInitialized(@NotNull Initializeable initializeable, int progress, int total) {
            subscriber.onNext(String.format(Locale.getDefault(), "Loaded %s component (%d/%d)",
                    initializeable.getClass().getSimpleName(), progress, total));
            subscriber.onCompleted();
        }

        @Override
        public void onException(@NotNull Initializeable initializeable, @NotNull Throwable th) {
            subscriber.onNext(String.format(Locale.getDefault(), "Failed to load %s component: %s",
                    initializeable.getClass().getSimpleName(), th.getMessage()));
        }

        @Override
        public void onFailure(@NotNull Initializeable initializeable, @NotNull Throwable th) {
            subscriber.onNext(String.format(Locale.getDefault(), "Operation failed: %s", th.getMessage()));
            getView().hideProgressView();
            subscriber.onCompleted();
        }

        @Override
        public void onFailure(@NotNull Throwable th) {
            subscriber.onNext(String.format(Locale.getDefault(), "Operation failed: %s", th.getMessage()));
            getView().hideProgressView();
            subscriber.onCompleted();
        }

    }

    @Inject
    public LoginPresenter(Context context, ILoginModel model) {
        this.model = model;
        this.context = context;
    }

    @Override
    public Observable<String> onLogin(String login, String password) {

        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                model.login(login, password, new LoginCallbackImp(subscriber));
            }
        });
    }

    @Override
    public void onNavigateCreateAccount() {
        callback.onCreateAccount();
    }

    @Override
    protected void onViewAttached(@NotNull LoginFragment view, @Nullable Bundle savedState, @Nullable Bundle data) {

        try {
            callback = (IInitialScreenView.NavigationListener) view.getActivity();
        } catch (final ClassCastException e) {
            throw new RuntimeException(
                    String.format("%s should implement %s interface!", view.getContext().toString(),
                            IInitialScreenView.NavigationListener.class.getSimpleName()));
        }
    }

    @Override
    protected void onDestroyed() {
        callback = null;
    }
}
