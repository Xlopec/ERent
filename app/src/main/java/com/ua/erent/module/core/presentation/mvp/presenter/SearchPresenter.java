package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.ua.erent.module.core.presentation.mvp.model.SearchForm;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ISearchModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ISearchPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.AdvancedSearchFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

import rx.Subscription;

/**
 * Created by Максим on 12/5/2016.
 */

public final class SearchPresenter extends ISearchPresenter {

    private static final String TAG = SearchPresenter.class.getSimpleName();
    private static final String ARG_FORM_CACHE = "argFormCache";

    private final ISearchModel model;
    private final Collection<Subscription> subscriptions;

    private SearchForm form;

    public SearchPresenter(ISearchModel model) {
        this.model = model;
        this.subscriptions = new ArrayList<>();
    }

    @Override
    protected void onViewAttached(@NotNull AdvancedSearchFragment view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            form = new SearchForm();

            setupCategories();
            setupBrands();
            setupRegions();
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        //outState.putParcelable(ARG_FORM_CACHE, form);
        super.onSaveInstanceState(outState);
    }

    private void setupCategories() {

        if (isViewGone())
            throw new IllegalStateException();

        final Collection<CategoryModel> categoryModels = model.getCategories();

        if (categoryModels.isEmpty()) {
            // no cached categories available
            if (model.hasConnection()) {
                // pull them from the api server
                model.fetchCategories()
                        .doOnSubscribe(getView()::showCategoriesProgress)
                        .subscribe(categories -> {
                                    if (!isViewGone()) {
                                        getView().setCategories(categories);
                                    }
                                },
                                th -> {
                                    if (!isViewGone()) {
                                        getView().showCategoriesFailure();
                                    }
                                    Log.w(TAG, "error on fetch categories#", th);
                                });
            } else {
                getView().showCategoriesFailure();
            }
        } else {
            getView().setCategories(categoryModels);
        }
    }

    private void setupBrands() {

        if (isViewGone())
            throw new IllegalStateException();

        if (model.hasConnection()) {

            model.fetchBrands()
                    .doOnSubscribe(getView()::showBrandsProgress)
                    .subscribe(brands -> {
                        if (!isViewGone()) {
                            getView().setBrands(brands);
                        }
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showBrandsFailure();
                        }
                        Log.e(TAG, "error on fetch brands#", th);
                    });
        } else {

            getView().showBrandsFailure();

            final Subscription subscription = model.getNetworkObservable()
                    .filter(hasConnection -> hasConnection)
                    .flatMap(conn -> model.fetchBrands())
                    .subscribe(brands -> {
                        if (!isViewGone()) {
                            getView().setBrands(brands);
                        }
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showBrandsFailure();
                        }
                        Log.e(TAG, "error on fetch brands#", th);
                    });

            subscriptions.add(subscription);
        }
    }

    private void setupRegions() {

        if (isViewGone())
            throw new IllegalStateException();

        if (model.hasConnection()) {

            model.fetchRegions()
                    .doOnSubscribe(getView()::showRegionsProgress)
                    .subscribe(regions -> {
                        if (!isViewGone()) {
                            getView().setRegions(regions);
                        }
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showRegionsFailure();
                        }
                        Log.e(TAG, "error on fetch regions#", th);
                    });
        } else {

            getView().showRegionsFailure();

            final Subscription subscription = model.getNetworkObservable()
                    .filter(hasConnection -> hasConnection)
                    .flatMap(connection -> model.fetchRegions())
                    .subscribe(regions -> {
                        if (!isViewGone()) {
                            getView().setRegions(regions);
                        }
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showRegionsFailure();
                        }
                        Log.e(TAG, "error on fetch regions#", th);
                    });

            subscriptions.add(subscription);
        }
    }

    @Override
    public void onCategorySelectionChanged(long id, boolean selected) {
        if (selected) {
            form.addCategory(id);
        } else {
            form.removeCategory(id);
        }
    }

    @Override
    public void onBrandSelectionChanged(long id, boolean selected) {
        if (selected) {
            form.addBrand(id);
        } else {
            form.removeBrand(id);
        }
    }

    @Override
    public void onRegionSelectionChanged(long id, boolean selected) {
        if (selected) {
            form.addRegion(id);
        } else {
            form.removeRegion(id);
        }
    }

    @Override
    public void onSearch(@Nullable String query) {
        form.setQuery(query);
        model.search(form, 10).subscribe(result -> {
            getView().setResult(result);
            getView().showResult();
        });
    }

    @Override
    public void onNextResult() {

    }

    @Override
    protected void onDestroyed() {
        for (final Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }
    }

}
