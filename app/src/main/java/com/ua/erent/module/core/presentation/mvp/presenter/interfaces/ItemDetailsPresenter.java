package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import android.widget.ImageView;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.ItemDetailsActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 12/3/2016.
 */

public abstract class ItemDetailsPresenter extends IBasePresenter<ItemDetailsActivity> {

    public static final String ARG_ITEM = "argItem";

    public abstract void onShowGallery(@NotNull ImageView clicked);

    public abstract void onOpenDialog();

    public abstract void onComplain();

}
