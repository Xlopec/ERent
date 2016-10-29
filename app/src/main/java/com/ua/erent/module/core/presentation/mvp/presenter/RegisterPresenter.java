package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;

import com.ua.erent.module.core.presentation.mvp.model.IRegisterModel;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;
import com.ua.erent.module.core.presentation.mvp.view.IInitialScreenView;
import com.ua.erent.module.core.presentation.mvp.view.RegisterFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Максим on 10/27/2016.
 */

public final class RegisterPresenter extends IRegisterPresenter {

    private IInitialScreenView.NavigationListener callback;

    private final IRegisterModel model;

    public RegisterPresenter(IRegisterModel model) {
        this.model = model;
    }

    @Override
    protected void onViewAttached(@NotNull RegisterFragment view, @Nullable Bundle savedState, @Nullable Bundle data) {

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

    @Override
    public void onNavigateLogin() {
        callback.onLogin();
    }

    @Override
    public void onCreateAccount(@NotNull SignUpForm form) {
        getView().showProgress("Registering in progress...");

        model.signUp(form).subscribe(
                aVoid -> {
                    getView().showToast("You've successfully registered");
                    getView().hideProgress();
                },
                th -> {
                    getView().showToast(th.getMessage());
                    getView().hideProgress();
                }
        );
    }
}
