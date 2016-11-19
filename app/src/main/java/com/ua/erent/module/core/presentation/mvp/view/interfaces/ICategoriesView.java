package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/12/2016.
 */

public interface ICategoriesView extends IBaseView {

    void addCategory(@NotNull CategoryModel model);

    void addCategory(@NotNull Collection<CategoryModel> models);

    void clearCategories();

    void hideRefreshProgress();

    void showRefreshProgress();

}
