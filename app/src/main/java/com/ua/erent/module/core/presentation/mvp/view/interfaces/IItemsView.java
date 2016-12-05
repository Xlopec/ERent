package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import android.widget.ImageView;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/13/2016.
 */

public interface IItemsView extends IBaseView {

    void setTitle(@NotNull String title);

    void showGallery(Collection<? extends IUrlFutureBitmap> futureBitmaps, ImageView imageView);

    void showMessage(@NotNull String message);

    void showProgressStart();

    void hideProgressStart();

    void showProgressEnd();

    void hideProgressEnd();

    void showProgress();

    void hideProgress();

    void setItems(@NotNull Collection<ItemModel> items);

    void addNextItems(@NotNull Collection<ItemModel> items);

    void addPrevItems(@NotNull Collection<ItemModel> items);

    void showText(@NotNull String string);

    void setInfoMessageVisible(boolean visible);

    void setInfoMessage(String text);

    void hideKeyboard();

}
