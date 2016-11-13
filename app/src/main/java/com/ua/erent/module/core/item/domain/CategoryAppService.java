package com.ua.erent.module.core.item.domain;

import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.storage.ICategoriesStorage;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoryAppService implements ICategoryAppService {

    private final ICategoriesStorage storage;
    private final ICategoryDomain domain;

    @Inject
    public CategoryAppService(ICategoriesStorage storage, ICategoryDomain domain) {
        this.storage = storage;
        this.domain = domain;
    }

    @Override
    public Collection<Category> getCachedCategories() {
        return storage.getAll();
    }

    @Override
    public Observable<Collection<Category>> fetchCategories() {
        return domain.fetchCategories();
    }

    @Override
    public Observable<Collection<Category>> getOnCategoriesAddedObs() {
        return domain.getOnCategoriesAddedObs();
    }

    @Override
    public Observable<Collection<Category>> getOnCategoriesDeletedObs() {
        return domain.getOnCategoriesDeletedObs();
    }
}
