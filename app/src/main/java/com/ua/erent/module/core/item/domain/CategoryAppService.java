package com.ua.erent.module.core.item.domain;

import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.storage.ICategoriesStorage;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoryAppService implements ICategoryAppService {
    public CategoryAppService(ICategoriesStorage storage) {

    }

    @Override
    public Collection<Category> getCachedCategories() {
        return null;
    }

    @Override
    public Observable<Collection<Category>> fetchCategories() {
        return null;
    }

    @Override
    public Observable<Collection<Category>> getOnCategoriesAddedObs() {
        return null;
    }

    @Override
    public Observable<Collection<Category>> getOnCategoriesDeletedObs() {
        return null;
    }
}
