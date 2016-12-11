package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 12/11/2016.
 */

public interface IMyItemsView extends IBaseView {

    void setItems(@NotNull Collection<ItemModel> items);

    void addItemsStart(@NotNull Collection<ItemModel> items);

    void addItemsEnd(@NotNull Collection<ItemModel> items);

    void showProgress();

    void hideProgress();

    void setInfoMessageVisible(boolean visible);

    void setInfoMessage(String text);

    void hideKeyboard();

    void showMessage(@NotNull String message);

    void hideProgressStart();

    void hideProgressEnd();
}
