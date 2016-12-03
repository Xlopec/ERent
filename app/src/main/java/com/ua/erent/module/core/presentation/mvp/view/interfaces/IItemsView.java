package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import android.widget.ImageView;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/13/2016.
 */

public interface IItemsView extends IBaseView {

    void showGallery(Collection<? extends IParcelableFutureBitmap> futureBitmaps, ImageView imageView);

    void showMessage(@NotNull String message);

    void showProgressStart();

    void hideProgressStart();

    void showProgressEnd();

    void hideProgressEnd();

    void showProgress();

    void hideProgress();

    void addNextItems(@NotNull Collection<ItemModel> items);

    void addPrevItems(@NotNull Collection<ItemModel> items);

}
