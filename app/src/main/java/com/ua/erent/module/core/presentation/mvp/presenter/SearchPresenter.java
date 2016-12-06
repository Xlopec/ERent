package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.ISearchModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ISearchPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.AdvancedSearchFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by Максим on 12/5/2016.
 */

public final class SearchPresenter extends ISearchPresenter {

    private static final String TAG = SearchPresenter.class.getSimpleName();

    private final ISearchModel model;

    public SearchPresenter(ISearchModel model) {
        this.model = model;
    }

    @Override
    protected void onViewAttached(@NotNull AdvancedSearchFragment view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            final Collection<CategoryModel> categoryModels = model.getCategories();

            if (categoryModels.isEmpty()) {
                // no cached categories available
                if (model.hasConnection()) {
                    // pull them from the api server
                    model.fetchCategories()
                            .subscribe(categories -> {
                                        if (!isViewGone()) {
                                            getView().setCategories(categories);
                                        }
                                    },
                                    th -> {
                                        if (!isViewGone()) {
                                            //getView().showMessage(th.getMessage());
                                           // syncWithView(categoryModels);
                                          //  getView().hideRefreshProgress();
                                        }
                                        Log.w(TAG, "error on fetch categories#", th);
                                    });
                } else {
                    Log.i(TAG, "fuck");
                }
            } else {
                view.setCategories(categoryModels);
            }

            model.fetchRegions()
                    .subscribe(regions -> {
                        if (!isViewGone()) {
                            getView().setRegions(regions);
                        }
                    }, th -> {
                        Log.e(TAG, "error on fetch regions#", th);
                    });

            model.fetchBrands()
                    .subscribe(brands -> {
                        if (!isViewGone()) {
                            getView().setBrands(brands);
                        }
                    }, th -> {
                        Log.e(TAG, "error on fetch brands#", th);
                    });
        }
    }

    @Override
    protected void onDestroyed() {

    }
}
