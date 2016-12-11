package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import android.content.Intent;
import android.net.Uri;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.ItemCreateActivity;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ItemCreationView;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 12/9/2016.
 */

public abstract class ItemCreationPresenter extends IBasePresenter<ItemCreateActivity> {

    public abstract Collection<ItemCreationView.SelectableModel> onGetCategories();

    public abstract Collection<ItemCreationView.SelectableModel> onGetBrands();

    public abstract Collection<ItemCreationView.SelectableModel> onGetRegions();

    public abstract void onSetCategory(long id);

    public abstract void onSetRegion(long id);

    public abstract void onSetBrand(long id);

    public abstract void onCreateItem(@NotNull String name, @NotNull String description,
                                      @NotNull String price);

    public abstract void onRefresh();

    public abstract void onDeleteImage(@NotNull Uri uri);

    public abstract void onDeleteImages();

    public abstract void onSelectImage();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

}
