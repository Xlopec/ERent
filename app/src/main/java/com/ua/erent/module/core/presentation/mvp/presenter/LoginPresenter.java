package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ILoginModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ILoginPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IInitialScreenView;
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

    private class InitCallbackImp implements IInitCallback {

        private final Subscriber<? super String> subscriber;

        public InitCallbackImp(Subscriber<? super String> subscriber) {
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

               // intent.setAction(Intent.ACTION_MAIN);
               // intent.addCategory(Intent.CATEGORY_LAUNCHER);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
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

            String message;

            if(TextUtils.isEmpty(th.getMessage())) {
                message = "Operation failed";
            } else {
                message = String.format(Locale.getDefault(), "Operation failed: %s", th.getMessage());
            }

            getView().showToast(message);
            subscriber.onNext(message);
            getView().hideProgressView();
            subscriber.onCompleted();
        }

        @Override
        public void onFailure(@NotNull Throwable th) {

            String message;

            if(TextUtils.isEmpty(th.getMessage())) {
                message = "Operation failed";
            } else {
                message = String.format(Locale.getDefault(), "Operation failed: %s", th.getMessage());
            }

            getView().showToast(message);
            subscriber.onNext(message);
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

        if(model.isSessionAlive()) {
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
