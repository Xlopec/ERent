package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import android.net.Uri;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/28/2016.
 */

public interface IRegisterView extends IBaseView {

    void setAvatarUri(@NotNull Uri uri);

    void showProgress(String message);

    void hideProgress();

    void showToast(String message);

}
