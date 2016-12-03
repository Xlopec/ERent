package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.ItemsActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Максим on 10/14/2016.
 */

public class ItemsPresenter extends IItemsPresenter {

    private static final String TAG = ItemsPresenter.class.getSimpleName();
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
                        //    addStart(items);
                    }
                });
    }

    @Override
    protected void onViewAttached(@NotNull ItemsActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            if (savedState == null) {

                model.fetch(PAGE_SIZE)
                        .doOnSubscribe(getView()::showProgress)
                        .doOnCompleted(getView()::hideProgress)
                        .subscribe(this::addEnd,
                                th -> {
                                    if (!isViewGone()) {
                                        getView().showMessage(th.getMessage());
                                        getView().hideProgress();
                                    }
                                    Log.e(TAG, "error", th);
                                });
            } else {

                final Parcelable[] cachedArr = savedState.getParcelableArray(ARG_CACHE);

                if (cachedArr == null)
                    throw new IllegalStateException("state wasn't saved!");

                final ArrayList<ItemModel> cache = new ArrayList<>(cachedArr.length);

                for(final Parcelable parcelable : cachedArr) {
                    cache.add((ItemModel) parcelable);
                }

                addEnd(cache);
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
    public void onPhotoClicked(long id, @NotNull ImageView imageView) {
        for (final ItemModel item : localCache) {

            if (item.getId() == id) {
                getView().showGallery(item.getGallery(), imageView);
                break;
            }
        }
    }

    @Override
    public void onLoadNext() {

        final ItemModel item = localCache.peekFirst();
        final Observable<Collection<ItemModel>> obs;

        obs = item == null ? model.fetch(PAGE_SIZE) : model.fetchNext(PAGE_SIZE, item.getId());

        obs.subscribe(this::addStart,
                th -> {
                    if (!isViewGone()) {
                        getView().showMessage(th.getMessage());
                        getView().hideProgressStart();
                    }
                    Log.e(TAG, "error", th);
                });
    }

    @Override
    public void onLoadPrev() {

        final ItemModel item = localCache.peekLast();
        final Observable<Collection<ItemModel>> obs;

        obs = item == null ? model.fetch(PAGE_SIZE) : model.fetchPrev(PAGE_SIZE, item.getId());

        obs.subscribe(this::addEnd,
                th -> {
                    if (!isViewGone()) {
                        getView().showMessage(th.getMessage());
                        getView().hideProgressEnd();
                    }
                    Log.e(TAG, "error", th);
                });
    }

    @Override
    public void onRefresh() {
        model.fetch(PAGE_SIZE)
                .doOnSubscribe(getView()::showProgress)
                .doOnCompleted(getView()::hideProgress)
                .subscribe(this::addStart,
                        th -> {
                            if (!isViewGone()) {
                                getView().showMessage(th.getMessage());
                                getView().hideProgress();
                            }
                            Log.e(TAG, "error", th);
                        });
    }

    private void addStart(Collection<ItemModel> arg) {

        if (isViewGone())
            throw new IllegalStateException();

        if (arg == null)
            throw new NullPointerException();

        if (!arg.isEmpty()) {

            final List<ItemModel> l;

            if (arg instanceof ArrayList) {
                l = (ArrayList<ItemModel>) arg;
            } else {
                l = new ArrayList<>(arg);
            }

            int i = 0;

            for (; i < MAX_VIEW_CACHE_SIZE && i < l.size(); ++i) {
                localCache.addFirst(l.get(i));
            }

            final List<ItemModel> tmp = l.subList(0, i);

            Collections.reverse(tmp);
            getView().addNextItems(tmp);
        } else {
            getView().addNextItems(arg);
        }
    }

    private void addEnd(Collection<ItemModel> c) {

        if (isViewGone())
            throw new IllegalStateException();

        if (c == null)
            throw new NullPointerException();

        if (c.size() > MAX_VIEW_CACHE_SIZE) {

            List<ItemModel> l;

            if (c instanceof List) {
                l = (List<ItemModel>) c;
            } else {
                l = new ArrayList<>(c);
            }

            c = l.subList(0, MAX_VIEW_CACHE_SIZE);
        }
        localCache.addAll(c);
        getView().addPrevItems(c);
    }


}
