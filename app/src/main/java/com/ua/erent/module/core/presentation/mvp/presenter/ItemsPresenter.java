package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.ua.erent.BuildConfig;
import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
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

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Максим on 10/14/2016.
 */

public final class ItemsPresenter extends IItemsPresenter {

    private static final String TAG = ItemsPresenter.class.getSimpleName();
    private static final String ARG_LOCAL_CACHE = "argLocalCache";
    private static final String ARG_QUERY_DEQ_CACHE = "argQueryDequeCache";
    private static final String ARG_CATEGORY_CACHE = "argCategoryCache";
    private static final String ARG_QUERY_CACHE = "argQueryCache";

    private static final int MAX_VIEW_CACHE_SIZE = 40;
    private static final int PAGE_SIZE = 5;

    private final Deque<ItemModel> queryCache;
    private final Deque<ItemModel> localCache;
    private final IItemsModel model;

    private final Subscription itemAddedSub;

    private CategoryModel category;
    private String query;

    @Inject
    public ItemsPresenter(IItemsModel model) {
        this.model = model;
        this.localCache = new ArrayDeque<>(MAX_VIEW_CACHE_SIZE);
        this.queryCache = new ArrayDeque<>(MAX_VIEW_CACHE_SIZE);

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

                category = data.getParcelable(IItemsPresenter.ARG_CATEGORY);
                Preconditions.checkNotNull(category, "category wasn't passed");

                view.setTitle(category.getTitle());
                model.fetch(category.getId(), PAGE_SIZE)
                        .doOnSubscribe(getView()::showProgress)
                        .doOnCompleted(getView()::hideProgress)
                        .subscribe(items -> {
                                    if (!isViewGone()) {
                                        if (items.isEmpty()) {
                                            getView().setInfoMessageVisible(true);
                                            getView().setInfoMessage(getView().getString(R.string.items_empty));
                                        } else {
                                            getView().setInfoMessageVisible(false);
                                            getView().addNextItems(addStart(items, localCache));
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
                final Parcelable[] queryArr = savedState.getParcelableArray(ARG_QUERY_DEQ_CACHE);

                query = savedState.getString(ARG_QUERY_CACHE);
                category = savedState.getParcelable(ARG_CATEGORY_CACHE);

                Preconditions.checkNotNull(category, "category wasn't saved");
                view.setTitle(category.getTitle());

                final ArrayList<ItemModel> localBundleCache = getFromCache(cachedArr);
                final ArrayList<ItemModel> localQueryCache = getFromCache(queryArr);

                if (query == null) {
                    addStart(localQueryCache, queryCache);
                    view.setItems(addStart(localBundleCache, localCache));
                } else {
                    addStart(localBundleCache, localCache);
                    view.setItems(addStart(localQueryCache, queryCache));
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelableArray(ARG_LOCAL_CACHE, localCache.toArray(new ItemModel[localCache.size()]));
        outState.putParcelableArray(ARG_QUERY_DEQ_CACHE, queryCache.toArray(new ItemModel[queryCache.size()]));
        outState.putParcelable(ARG_CATEGORY_CACHE, category);
        outState.putString(ARG_QUERY_CACHE, query);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {
        itemAddedSub.unsubscribe();
        localCache.clear();
    }

    @Override
    public void onSearch(@NotNull String query) {
        this.query = query;
        getView().hideKeyboard();

        model
                .search(query, PAGE_SIZE)
                .doOnSubscribe(getView()::showProgress)
                .doOnCompleted(getView()::hideProgress)
                .subscribe(items -> {
                            if (!isViewGone()) {
                                if (items.isEmpty()) {
                                    getView().setInfoMessageVisible(true);
                                    getView().setInfoMessage(getView().getString(R.string.items_search_not_found));
                                } else {
                                    getView().setInfoMessageVisible(false);
                                }
                                getView().setItems(addStart(items, queryCache));
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

    @Override
    public void onBackButton() {

        final ItemsActivity view = getView();

        if (query == null) {
            view.finish();
        } else {
            query = null;
            queryCache.clear();
            view.setItems(localCache);
        }
    }

    @Override
    public void onItemClicked(long id) {
        for (final ItemModel item : localCache) {

            if (item.getId() == id) {
                getView().startActivity(model.createItemDetailsIntent(item));
                break;
            }
        }
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

        final Deque<ItemModel> dest;
        final Observable<Collection<ItemModel>> obs;

        if (query != null) {
            final ItemModel item = queryCache.peekFirst();

            dest = queryCache;
            obs = item == null ? model.search(query, PAGE_SIZE)
                    : model.fetchNext(query, PAGE_SIZE, item.getId());

        } else {
            final ItemModel item = localCache.peekFirst();

            dest = localCache;
            obs = item == null ? model.fetch(category.getId(), PAGE_SIZE)
                    : model.fetchNext(category.getId(), PAGE_SIZE, item.getId());
        }

        obs.subscribe(items -> {
                    if (!isViewGone()) {
                        getView().addNextItems(addStart(items, dest));
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

    @Override
    public void onLoadPrev() {

        final Observable<Collection<ItemModel>> obs;
        final Deque<ItemModel> dest;

        if (query != null) {
            final ItemModel item = queryCache.peekLast();

            dest = queryCache;
            obs = item == null ? model.search(query, PAGE_SIZE)
                    : model.fetchPrev(query, PAGE_SIZE, item.getId());

        } else {
            final ItemModel item = localCache.peekLast();

            dest = localCache;
            obs = item == null ? model.fetch(category.getId(), PAGE_SIZE)
                    : model.fetchPrev(category.getId(), PAGE_SIZE, item.getId());
        }

        obs.subscribe(items -> {
                    if (!isViewGone()) {
                        addEnd(items, dest);
                    }
                },
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

        final Action1<Collection<ItemModel>> refreshDefAction = items -> {
            if (!isViewGone()) {
                if (items.isEmpty()) {
                    getView().setInfoMessageVisible(true);
                    getView().setInfoMessage(getView().getString(R.string.items_empty));
                } else {
                    getView().setInfoMessageVisible(false);
                    getView().addNextItems(addStart(items, localCache));
                }
            }
        };

        final Action1<Collection<ItemModel>> refreshSearchAction = items -> {
            if (!isViewGone()) {
                if (items.isEmpty()) {
                    getView().setInfoMessageVisible(true);
                    getView().setInfoMessage(getView().getString(R.string.items_search_not_found));
                } else {
                    getView().setInfoMessageVisible(false);
                    getView().addNextItems(addStart(items, queryCache));
                }
            }
        };

        final Observable<Collection<ItemModel>> obs;

        if (query != null) {

            final ItemModel item = queryCache.peekFirst();
            obs = item == null ? model.search(query, PAGE_SIZE)
                    : model.fetchNext(query, PAGE_SIZE, item.getId());
        } else {

            final ItemModel item = localCache.peekFirst();
            obs = item == null ? model.fetch(category.getId(), PAGE_SIZE)
                    : model.fetchNext(category.getId(), PAGE_SIZE, item.getId());
        }

        obs
                .doOnSubscribe(getView()::showProgress)
                .doOnCompleted(getView()::hideProgress)
                .subscribe(query == null ? refreshDefAction : refreshSearchAction,
                        th -> {
                            if (!isViewGone()) {
                                getView().showMessage(th.getMessage());
                                getView().hideProgress();
                            }
                            Log.e(TAG, "error", th);
                        });
    }

    @Override
    public int getPopupResId() {
        return model.isSessionAlive() ? R.menu.items_authorized_menu : R.menu.items_unauthorized_menu;
    }

    @Override
    public void onOpenDialog(long id) {

    }

    @Override
    public void onComplain(long id) {

        for (final ItemModel itemModel : localCache) {

            if (itemModel.getId() == id) {
                final Context context = getView();
                final Intent intent = model.createComplainIntent(context.getString(R.string.support_email),
                        context.getString(R.string.item_details_complaint_subj, itemModel.getUsername()),
                        context.getString(R.string.item_details_complaint_body, itemModel.getId(), itemModel.getUsername()));

                try {
                    context.startActivity(Intent.createChooser(intent, context.getString(R.string.item_details_email_client_chooser)));
                } catch (final ActivityNotFoundException e) {
                    getView().showText(context.getString(R.string.item_details_no_email_client));
                }
                break;
            }
        }
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

        if (BuildConfig.DEBUG) {
            ensureValidState(dest);
        }
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

        if (BuildConfig.DEBUG) {
            ensureValidState(dest);
        }

        getView().addPrevItems(c);
    }

    private static void ensureValidState(Iterable<ItemModel> it) {

        ItemModel prev = null;

        for (final ItemModel model : it) {
            if (prev != null) {
                if (model.getId() >= prev.getId()) {
                    throw new IllegalStateException("invalid id");
                }
            }
            prev = model;
        }
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
