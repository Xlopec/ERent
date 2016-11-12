package com.ua.erent.module.core.presentation.mvp.model;

import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesModel implements ICategoriesModel {

    private final ICategoryAppService categoryAppService;

    @Inject
    public CategoriesModel(ICategoryAppService categoryAppService) {
        this.categoryAppService = categoryAppService;
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesDeletedObs() {
        return null;
    }

    @Override
    public Observable<Collection<CategoryModel>> fetchCategories() {
        return null;
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesAddedObs() {
        return null;
    }

    /*private static Collection<CategoryModel> toModel(Collection<Category> categories) {

        final Collection<CategoryModel> result = new ArrayList<>(categories.size());

        for(final Category category : categories) {
            result.add(new CategoryModel())
        }

        return result;
    }*/

}
