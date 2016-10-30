package com.ua.erent.module.core.presentation.mvp.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.IRegisterModel;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IRegisterPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IInitialScreenView;
import com.ua.erent.module.core.presentation.mvp.view.RegisterFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rx.Observable;

/**
 * Created by Максим on 10/27/2016.
 */

public final class RegisterPresenter extends IRegisterPresenter {

    private final String ARG_AVATAR_URI_STATE = "argAvatarUriState";

    private Uri avatarUri;
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

        if(savedState != null) {
            restoreState(savedState);
        }
    }

    private void restoreState(Bundle state) {

        avatarUri = state.getParcelable(ARG_AVATAR_URI_STATE);

        if(avatarUri != null) {
            getView().setAvatarUri(avatarUri);
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_AVATAR_URI_STATE, avatarUri);
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

        model.signUp(form.setAvatarUri(avatarUri)).subscribe(
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

    @Override
    public Observable<Bitmap> resizeAvatarBitmap(@NotNull Uri uri, @NotNull Bitmap original, int h, int w) {
        avatarUri = uri;
        return model.resizeBitmap(original, h, w);
    }
}
