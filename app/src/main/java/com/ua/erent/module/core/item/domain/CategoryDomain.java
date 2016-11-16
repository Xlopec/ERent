package com.ua.erent.module.core.item.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.item.domain.api.ICategoriesProvider;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.storage.ICategoriesStorage;
import com.ua.erent.module.core.item.sync.ICategorySynchronized;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoryDomain implements ICategoryDomain, ICategorySynchronized {

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
        return provider.fetchCategories()
                .flatMap(categories -> {
                    synchronized (storage) {
                        storage.store(categories);
                    }
                    return Observable.just(categories);
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Collection<Category>> getOnCategoriesAddedObs() {
        return addedObs.observeOn(AndroidSchedulers.mainThread()).asObservable();
    }

    @Override
    public Observable<Collection<Category>> getOnCategoriesDeletedObs() {
        return deletedObs.observeOn(AndroidSchedulers.mainThread()).asObservable();
    }

    @Override
    public Observable<Initializeable> initialize(@NotNull Session session) {
        return provider.fetchCategories()
                .flatMap(new Func1<Collection<Category>, Observable<Initializeable>>() {

                    @Override
                    public Observable<Initializeable> call(Collection<Category> categories) {
                        synchronized (storage) {
                            storage.store(categories);
                        }
                        return Observable.just(CategoryDomain.this);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onReject() {
        storage.clear();
    }

    @Override
    public boolean failOnException() {
        return false;
    }

    @Override
    public void synchronize(@NotNull Collection<Category> update) {

        new Thread(() -> {

            synchronized (storage) {

                Collection<Category> stored = storage.getAll();

                if (stored.isEmpty() || !update.equals(stored)) {

                    final Collection<Category> added = new ArrayList<>(update);
                    added.removeAll(stored);

                    final Collection<Category> removed = new ArrayList<>(stored);
                    removed.removeAll(update);

                    addedObs.onNext(added);
                    deletedObs.onNext(removed);
                    storage.store(update);
                }
            }
        }).start();
    }

}
