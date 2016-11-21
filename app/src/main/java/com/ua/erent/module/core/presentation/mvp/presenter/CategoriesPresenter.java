package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.ua.erent.R;
import com.ua.erent.module.core.networking.util.ConnectionManager;
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

import dagger.internal.Preconditions;

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
    private final ConnectionManager connectionManager;

    @Inject
    public CategoriesPresenter(ICategoriesModel model, ConnectionManager connectionManager) {
        this.model = model;
        this.connectionManager = connectionManager;
        this.localCache = new ArrayList<>(0);
    }

    @Override
    protected void onViewAttached(@NotNull CategoriesActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            final Collection<CategoryModel> categoryModels = model.getCategories();

            if (savedState == null) {

                if (connectionManager.hasConnection()) {
                    // no cached categories available,
                    // pull them from the api server
                    model.fetchCategories()
                            .doOnSubscribe(view::showRefreshProgress)
                            .doOnCompleted(view::hideRefreshProgress)
                            .subscribe(this::syncWithView,
                                    th -> {
                                        if (!isViewGone()) {
                                            view.showMessage(th.getMessage());
                                            syncWithView(categoryModels);
                                            view.hideRefreshProgress();
                                        }
                                        Log.w(TAG, "error on fetch categories#", th);
                                    });
                } else if (!categoryModels.isEmpty()) {
                    syncWithView(categoryModels);
                } else {
                    view.showMessage(view.getString(R.string.categories_no_internet));
                }
            } else {
                syncWithView(savedState.getParcelableArrayList(CategoriesPresenter.ARG_CACHED_CATEGORIES));
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

        Preconditions.checkNotNull(categoryModels);

        final ICategoriesView view = getView();

        localCache.clear();
        localCache.addAll(categoryModels);
        view.clearCategories();
        view.addCategory(categoryModels);
    }

}
