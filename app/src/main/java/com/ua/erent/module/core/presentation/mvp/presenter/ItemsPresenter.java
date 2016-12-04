package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.ua.erent.R;
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
    private static final String ARG_CATEGORY_CACHE = "argCategoryCache";

    private static final int MAX_VIEW_CACHE_SIZE = 40;
    private static final int PAGE_SIZE = 7;

    private final Deque<ItemModel> localCache;
    private final IItemsModel model;

    private final Subscription itemAddedSub;

    private long categoryId;

    @Inject
    public ItemsPresenter(IItemsModel model) {
        this.model = model;
        this.localCache = new ArrayDeque<>(MAX_VIEW_CACHE_SIZE);
        this.categoryId = -1;

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

                categoryId = data.getLong(IItemsPresenter.ARG_CATEGORY_ID, -1);

                if (categoryId == -1)
                    throw new IllegalArgumentException("category id wasn't passed");

                model.fetch(categoryId, PAGE_SIZE)
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
                categoryId = savedState.getLong(IItemsPresenter.ARG_CATEGORY_ID, -1);

                if (cachedArr == null)
                    throw new IllegalStateException("state wasn't saved!");

                if (categoryId == -1)
                    throw new IllegalArgumentException("category id wasn't saved");

                final ArrayList<ItemModel> cache = new ArrayList<>(cachedArr.length);

                for (final Parcelable parcelable : cachedArr) {
                    cache.add((ItemModel) parcelable);
                }

                addEnd(cache);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelableArray(ARG_CACHE, localCache.toArray(new ItemModel[localCache.size()]));
        outState.putLong(ARG_CATEGORY_CACHE, categoryId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {
        itemAddedSub.unsubscribe();
        localCache.clear();
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

        final ItemModel item = localCache.peekLast();
        final Observable<Collection<ItemModel>> obs;

        obs = item == null ? model.fetch(categoryId, PAGE_SIZE) : model.fetchNext(categoryId, PAGE_SIZE, item.getId());

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

        final ItemModel item = localCache.peekFirst();
        final Observable<Collection<ItemModel>> obs;

        obs = item == null ? model.fetch(categoryId, PAGE_SIZE) : model.fetchPrev(categoryId, PAGE_SIZE, item.getId());

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

        final ItemModel item = localCache.peekLast();
        final Observable<Collection<ItemModel>> obs;

        obs = item == null ? model.fetch(categoryId, PAGE_SIZE) : model.fetchNext(categoryId, PAGE_SIZE, item.getId());

        obs
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
