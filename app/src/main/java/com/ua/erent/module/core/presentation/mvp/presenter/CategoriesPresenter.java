package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICategoriesPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ICategoriesView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.inject.Inject;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesPresenter extends ICategoriesPresenter {

    private static final String ARG_CACHED_CATEGORIES = "argCachedCategories";
    private static final String TAG = CategoriesPresenter.class.getSimpleName();

    private static final int[] CATEGORY_COLORS = {
            0xcc0000,// red
            0x333399,// blue
            0xff9900,//orange
            0x339933,//green
            0x00cccc//cyan
    };

    private final ICategoriesModel model;
    private final ArrayList<CategoryModel> localCache;

    @Inject
    public CategoriesPresenter(ICategoriesModel model) {
        this.model = model;
        this.localCache = new ArrayList<>(0);
    }

    @Override
    protected void onViewAttached(@NotNull CategoriesActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            Collection<CategoryModel> categoryModels = model.getCategories();

            if (savedState == null) {

                if (categoryModels.isEmpty()) {
                    // no cached categories available,
                    // pull them from the api server
                    model.fetchCategories().subscribe(this::syncWithView,
                            th -> {
                                if (!isViewGone()) {
                                    getView().showMessage(th.getMessage());
                                }
                                Log.w(TAG, "error on fetch categories#", th);
                            });
                } else {
                    syncWithView(categoryModels);
                }
            } else {
                categoryModels = savedState.getParcelableArrayList(CategoriesPresenter.ARG_CACHED_CATEGORIES);
                syncWithView(categoryModels);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelableArrayList(CategoriesPresenter.ARG_CACHED_CATEGORIES, localCache);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {

    }

    @Override
    public int getRandomColor() {
        return CATEGORY_COLORS[new Random().nextInt(CATEGORY_COLORS.length)];
    }

    @Override
    public void onOpenCategory(long categoryId) {

        System.out.println(categoryId);

    }

    @Override
    public void onRefresh() {

        model.fetchCategories().subscribe(categories -> {
                    if (!isViewGone()) {
                        syncWithView(categories);
                        getView().hideRefreshProgress();
                    }
                }, th -> {
                    if (!isViewGone()) {
                        getView().hideRefreshProgress();
                    }
                    Log.w(TAG, "error occurred on refresh#", th);
                }
        );
    }

    private void syncWithView(Collection<CategoryModel> categoryModels) {

        if (isViewGone())
            throw new IllegalStateException("nothing to sync with");

        final ICategoriesView view = getView();

        localCache.addAll(categoryModels);
        view.clearCategories();
        view.addCategory(categoryModels);
    }

}
