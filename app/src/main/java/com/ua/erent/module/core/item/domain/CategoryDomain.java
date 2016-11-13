package com.ua.erent.module.core.item.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.item.domain.api.ICategoriesProvider;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.storage.ICategoriesStorage;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoryDomain implements ICategoryDomain {

    private final ICategoriesStorage storage;
    private final ICategoriesProvider provider;
    private final PublishSubject<Collection<Category>> addedObs;
    private final PublishSubject<Collection<Category>> deletedObs;

    public CategoryDomain(ICategoriesStorage storage, ICategoriesProvider provider) {
        this.storage = storage;
        this.provider = provider;
        this.addedObs = PublishSubject.create();
        this.deletedObs = PublishSubject.create();
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

    @Override
    public Observable<Initializeable> initialize(@NotNull Session session) {
        return null;
    }

    @Override
    public void onReject() {

    }

    @Override
    public boolean failOnException() {
        return false;
    }

}
