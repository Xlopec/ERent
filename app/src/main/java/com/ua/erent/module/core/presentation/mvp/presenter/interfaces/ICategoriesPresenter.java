package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/12/2016.
 */

public abstract class ICategoriesPresenter extends IBasePresenter<CategoriesActivity> {

    public abstract int getRandomColor();

    public abstract void onOpenCategory(@NotNull CategoryModel categoryId);

    public abstract void onRefresh();

    public abstract void onLogin();

    public abstract void onLogout();

}
