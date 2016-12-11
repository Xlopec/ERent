package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IMyItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IMyItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.MyItemsActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import dagger.internal.Preconditions;
import rx.Observable;

/**
 * Created by Максим on 12/11/2016.
 */

public final class MyItemsPresenter extends IMyItemsPresenter {

    private static final String TAG = MyItemsPresenter.class.getSimpleName();

    private static final String ARG_LOCAL_CACHE = "argLocalCache";

    private static final int MAX_VIEW_CACHE_SIZE = 40;
    private static final int PAGE_SIZE = 5;

    private final IMyItemsModel model;

    private final Deque<ItemModel> localCache;

    public MyItemsPresenter(IMyItemsModel model) {
        this.model = model;
        this.localCache = new ArrayDeque<>(MAX_VIEW_CACHE_SIZE);
    }

    @Override
    protected void onViewAttached(@NotNull MyItemsActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            if (savedState == null) {

                model.fetch(PAGE_SIZE)
                        .doOnSubscribe(getView()::showProgress)
                        .doOnCompleted(getView()::hideProgress)
                        .subscribe(items -> {
                                    if (!isViewGone()) {
                                        if (items.isEmpty()) {
                                            getView().setInfoMessageVisible(true);
                                            getView().setInfoMessage(getView().getString(R.string.my_items_empty));
                                        } else {
                                            getView().setInfoMessageVisible(false);
                                            getView().addItemsStart(addStart(items, localCache));
                                        }
                                    }
                                },
                                th -> {
                                    if (!isViewGone()) {
                                        getView().showMessage(th.getMessage());
                                        getView().hideProgress();
                                    }
                                    Log.e(TAG, "error", th);
                                });
            } else {

                final Parcelable[] cachedArr = savedState.getParcelableArray(ARG_LOCAL_CACHE);
                final ArrayList<ItemModel> localBundleCache = getFromCache(cachedArr);

                view.setItems(addStart(localBundleCache, localCache));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelableArray(ARG_LOCAL_CACHE, localCache.toArray(new ItemModel[localCache.size()]));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {
        localCache.clear();
    }

    @Override
    public void onLoadNext() {

        final ItemModel item = localCache.peekFirst();
        final Observable<Collection<ItemModel>> obs = item == null
                ? model.fetch(PAGE_SIZE) : model.fetchNext(PAGE_SIZE, item.getId());

        obs
                .doOnTerminate(getView()::hideProgressStart)
                .subscribe(items -> {
                            if (!isViewGone()) {
                                getView().setInfoMessageVisible(false);
                                getView().addItemsStart(addStart(items, localCache));
                            }
                        },
                        th -> {
                            if (!isViewGone()) {
                                getView().showMessage(th.getMessage());
                            }
                            Log.e(TAG, "error", th);
                        });
    }

    @Override
    public void onLoadPrev() {

        final ItemModel item = localCache.peekLast();
        final Observable<Collection<ItemModel>> obs = item == null
                ? model.fetch(PAGE_SIZE) : model.fetchPrev(PAGE_SIZE, item.getId());

        obs
                .doOnTerminate(getView()::hideProgressEnd)
                .subscribe(items -> {
                            if (!isViewGone()) {
                                getView().setInfoMessageVisible(false);
                                addEnd(items, localCache);
                            }
                        },
                        th -> {
                            if (!isViewGone()) {
                                getView().showMessage(th.getMessage());
                            }
                            Log.e(TAG, "error", th);
                        });
    }

    @Override
    public void onCreateNew() {
        getView().startActivity(model.createNewItemIntent());
    }

    @Override
    public void onBackButton() {
        getView().finish();
    }

    @Override
    public void onRefresh() {

        final ItemModel item = localCache.peekFirst();
        final Observable<Collection<ItemModel>> obs = item == null
                ? model.fetch(PAGE_SIZE) : model.fetchNext(PAGE_SIZE, item.getId());

        obs
                .doOnSubscribe(getView()::showProgress)
                .doOnTerminate(getView()::hideProgress)
                .subscribe(items -> {
                            if (!isViewGone()) {
                                getView().setInfoMessageVisible(false);
                                getView().addItemsStart(addStart(items, localCache));
                            }
                        },
                        th -> {
                            if (!isViewGone()) {
                                getView().showMessage(th.getMessage());
                                getView().hideProgressStart();
                            }
                            Log.e(TAG, "error", th);
                        });
    }

    private Collection<ItemModel> addStart(Collection<ItemModel> arg, Deque<ItemModel> dest) {

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
                dest.addFirst(l.get(i));
            }

            final List<ItemModel> tmp = l.subList(0, i);

            Collections.reverse(tmp);
            arg = tmp;
        }

        /*if (BuildConfig.DEBUG) {
            ensureValidState(dest);
        }*/
        return arg;
    }

    private void addEnd(Collection<ItemModel> c, Deque<ItemModel> dest) {

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

        dest.addAll(c);
        /*if (BuildConfig.DEBUG) {
            ensureValidState(dest);
        }*/
        getView().addItemsEnd(c);
    }

    private static ArrayList<ItemModel> getFromCache(Parcelable[] parcelables) {
        Preconditions.checkNotNull(parcelables);
        final ArrayList<ItemModel> result = new ArrayList<>(parcelables.length);

        for (int i = parcelables.length - 1; i >= 0; --i) {
            result.add((ItemModel) parcelables[i]);
        }

        return result;
    }

}
