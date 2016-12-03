package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import android.widget.ImageView;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.ItemsActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/14/2016.
 */

public abstract class IItemsPresenter extends IBasePresenter<ItemsActivity> {

    public static final String ARG_CATEGORY_ID = IItemsPresenter.class.getName().concat(".argCategoryId");

    public abstract void onItemClicked(long id);

    public abstract void onPhotoClicked(long id, @NotNull ImageView imageView);

    public abstract void onLoadNext();

    public abstract void onLoadPrev();

    public abstract void onRefresh();

}
