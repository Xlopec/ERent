package com.ua.erent.module.core.item.domain;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.item.domain.api.ICategoriesProvider;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.storage.ICategoriesStorage;
import com.ua.erent.module.core.item.sync.CategorySynchronizeable;
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

public final class CategoryDomain implements ICategoryDomain {

    private final ICategoriesStorage storage;
    private final ICategoriesProvider provider;
    private final PublishSubject<Collection<Category>> addedObs;
    private final PublishSubject<Collection<Category>> deletedObs;

    public CategoryDomain(Application context, ICategoriesStorage storage, ICategoriesProvider provider) {
        this.storage = storage;
        this.provider = provider;
        this.addedObs = PublishSubject.create();
        this.deletedObs = PublishSubject.create();

        context.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                final Collection<Category> categories =
                        intent.getParcelableArrayListExtra(CategorySynchronizeable.ARG_RESULT);

                synchronize(categories);
            }
        }, new IntentFilter(CategorySynchronizeable.FILTER));
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

    private void synchronize(Collection<Category> update) {

        Collection<Category> stored = storage.getAll();

        if (stored.isEmpty() || !update.equals(stored)) {

            final Collection<Category> added = new ArrayList<>(update);
            added.removeAll(stored);

            final Collection<Category> removed = new ArrayList<>(stored);
            removed.removeAll(update);

            if (!added.isEmpty()) {
                addedObs.onNext(added);
            }

            if (!removed.isEmpty()) {
                deletedObs.onNext(removed);
            }
            storage.store(update);
        }
    }

}