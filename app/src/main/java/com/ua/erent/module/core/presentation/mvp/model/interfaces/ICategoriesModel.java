package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/12/2016.
 */

public interface ICategoriesModel {

    Collection<CategoryModel> getCategories();

    Observable<Collection<CategoryModel>> getOnCategoriesDeletedObs();

    Observable<Collection<CategoryModel>> fetchCategories();

    Observable<Collection<CategoryModel>> getOnCategoriesAddedObs();
}
