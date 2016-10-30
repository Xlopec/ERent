package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rx.Observable;

/**
 * Created by Максим on 10/28/2016.
 */

public interface ILoginView extends IBaseView {

    void bindToProgressView(@NotNull Observable<String> observable);

    void setLogin(@Nullable String login);

    void showToast(String message);

    void showProgressBar(String message);

    void hideProgressBar();

    void showProgressView();

    void setProgressInfo(String info);

    void hideProgressView();

}
