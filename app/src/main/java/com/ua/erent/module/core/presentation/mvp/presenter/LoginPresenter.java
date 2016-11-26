package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.ua.erent.R;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ILoginModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ILoginPresenter;
import com.ua.erent.module.core.presentation.mvp.view.LoginFragment;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IInitialScreenView;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private class InitCallbackImp implements IInitCallback {

        private final Subscriber<? super String> subscriber;

        InitCallbackImp(Subscriber<? super String> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onPreExecute() {
            subscriber.onStart();
            getView().showProgressView();
        }

        @Override
        public void onInitialized() {
            try {
                subscriber.onNext(context.getString(R.string.auth_loaded));
                getView().hideProgressView();
                getView().startActivity(model.createLoginIntent(context));
                getView().getActivity().finish();
            } finally {
                subscriber.onCompleted();
            }
        }

        @Override
        public void onComponentInitialized(@NotNull Initializeable initializeable, int progress, int total) {
            subscriber.onNext(context.getString(R.string.auth_component_loaded,
                    initializeable.getClass().getSimpleName(), progress, total));
        }

        @Override
        public void onException(@NotNull Initializeable initializeable, @NotNull Throwable th) {
            subscriber.onNext(context.getString(R.string.auth_component_load_err,
                    initializeable.getClass().getSimpleName(), th.getMessage()));
        }

        @Override
        public void onFailure(@NotNull Initializeable initializeable, @NotNull Throwable th) {
            try {
                String message;

                if (TextUtils.isEmpty(th.getMessage())) {
                    message = context.getString(R.string.auth_operation_failure);
                } else {
                    message = context.getString(R.string.auth_operation_failure_formatted, th.getMessage());
                }

                getView().showToast(message);
                subscriber.onNext(message);
                getView().hideProgressView();
            } finally {
                subscriber.onCompleted();
            }
        }

        @Override
        public void onFailure(@NotNull Throwable th) {
            try {
                String message;

                if (TextUtils.isEmpty(th.getMessage())) {
                    message = context.getString(R.string.auth_operation_failure);
                } else {
                    message = context.getString(R.string.auth_operation_failure_formatted, th.getMessage());
                }

                getView().showToast(message);
                subscriber.onNext(message);
                getView().hideProgressView();
            } finally {
                subscriber.onCompleted();
            }
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
                model.login(login, password, new InitCallbackImp(subscriber));
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

        view.setLogin(model.getLastLogin());

        if (model.isSessionAlive()) {
            view.bindToProgressView(Observable.create(subscriber -> {
                model.login(new InitCallbackImp(subscriber));
            }));
        }
    }

    @Override
    protected void onDestroyed() {
        callback = null;
    }
}
