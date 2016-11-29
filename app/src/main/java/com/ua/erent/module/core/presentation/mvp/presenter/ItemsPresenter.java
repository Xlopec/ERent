package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.ItemsActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Максим on 10/14/2016.
 */

public class ItemsPresenter extends IItemsPresenter {

    private static final String ARG_CACHE = "argCache";

    private static final int MAX_VIEW_CACHE_SIZE = 40;
    private static final int PAGE_SIZE = 7;

    private final Deque<ItemModel> localCache;
    private final IItemsModel model;

    private final Subscription itemAddedSub;

    @Inject
    public ItemsPresenter(IItemsModel model) {
        this.model = model;
        this.localCache = new ArrayDeque<>(MAX_VIEW_CACHE_SIZE);

        this.itemAddedSub = model.getOnItemAddedObs()
                .subscribe(items -> {
                    if (!isViewGone()) {
                        addStart(items);
                    }
                });
    }

    @Override
    protected void onViewAttached(@NotNull ItemsActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            if (savedState == null) {

                model.fetch(PAGE_SIZE)
                        .doOnSubscribe(view::showProgress)
                        .doOnCompleted(view::hideProgress)
                        .subscribe(this::addEnd,
                                th -> {
                                    view.showMessage(th.getMessage());
                                    Log.e("Tag", "error", th);
                                });
            } else {

                final ItemModel[] cachedArr = (ItemModel[]) savedState.getParcelableArray(ARG_CACHE);

                if (cachedArr == null)
                    throw new IllegalStateException("state wasn't saved!");

                addEnd(Arrays.asList(cachedArr));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelableArray(ARG_CACHE, localCache.toArray(new ItemModel[localCache.size()]));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {
        itemAddedSub.unsubscribe();
        localCache.clear();
    }

    @Override
    public void onItemClicked(long id) {

    }

    @Override
    public void onLoadNext() {

    }

    @Override
    public void onLoadPrev() {

    }

    private void addStart(Collection<ItemModel> arg) {

        if (isViewGone())
            throw new IllegalStateException();

        if (arg == null)
            throw new NullPointerException();

        final List<ItemModel> l;

        if (arg instanceof List) {
            l = (List<ItemModel>) arg;
        } else {
            l = new ArrayList<>(arg);
        }

        final ListIterator<ItemModel> iterator = l.listIterator(l.size() - 1);

        for (int i = 0; i < MAX_VIEW_CACHE_SIZE && iterator.hasPrevious(); ++i) {
            localCache.addFirst(iterator.previous());
        }
        getView().addNextItems(localCache);
    }

    private void addEnd(Collection<ItemModel> c) {

        if (isViewGone())
            throw new IllegalStateException();

        if (c == null)
            throw new NullPointerException();

        if (c.size() + localCache.size() <= MAX_VIEW_CACHE_SIZE) {
            localCache.addAll(c);
        } else {

            final Iterator<ItemModel> iterator = c.iterator();

            for (int i = 0; i < MAX_VIEW_CACHE_SIZE && iterator.hasNext(); ++i) {
                localCache.addLast(iterator.next());
            }
        }
        getView().addPrevItems(localCache);
    }


}
