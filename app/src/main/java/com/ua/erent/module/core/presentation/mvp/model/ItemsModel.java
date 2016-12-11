package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.content.Intent;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.api.filter.FilterBuilder;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.util.ItemConverter;
import com.ua.erent.module.core.presentation.mvp.view.ChatActivity;
import com.ua.erent.module.core.presentation.mvp.view.ItemDetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 11/28/2016.
 */

public final class ItemsModel implements IItemsModel {

    private final IItemAppService itemAppService;
    private final IAuthAppService authAppService;
    private final Context context;

    @Inject
    public ItemsModel(Context context, IItemAppService itemAppService, IAuthAppService authAppService) {
        this.itemAppService = itemAppService;
        this.context = context;
        this.authAppService = authAppService;
    }

    @Override
    public boolean isSessionAlive() {
        return authAppService.isSessionAlive();
    }

    @Override
    public Observable<Collection<ItemModel>> fetch(long categoryId, long limit) {
        return itemAppService.fetchItems(new FilterBuilder()
                .withCategory(categoryId)
                .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                .withLimit(limit)
                .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Observable<Collection<ItemModel>> fetchNext(long categoryId, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withLastIdGreater(lastId)
                        .withCategory(categoryId)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .sort(FilterBuilder.SortType.ASC)
                        .withLimit(limit)
                        .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Observable<Collection<ItemModel>> fetchPrev(long categoryId, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withCategory(categoryId)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .withLastIdLower(lastId)
                        .withLimit(limit)
                        .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Observable<Collection<ItemModel>> fetchNext(@NotNull String query, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withLastIdGreater(lastId)
                        .withQuery(query)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .sort(FilterBuilder.SortType.ASC)
                        .withLimit(limit)
                        .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Observable<Collection<ItemModel>> fetchPrev(@NotNull String query, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withLastIdLower(lastId)
                        .withQuery(query)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .sort(FilterBuilder.SortType.ASC)
                        .withLimit(limit)
                        .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Observable<Collection<ItemModel>> getOnItemAddedObs() {
        return itemAppService.getOnItemsAddedObs()
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Intent createItemDetailsIntent(@NotNull ItemModel item) {
        final Intent intent = new Intent(context, ItemDetailsActivity.class);
        intent.putExtra(ItemDetailsPresenter.ARG_ITEM, item);
        return intent;
    }

    @Override
    public Intent createComplainIntent(@NotNull String email, @NotNull String subject, @NotNull String body) {
        final Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }

    @Override
    public Intent createConversationIntent(long itemId, long userId) {

        final Intent intent = new Intent(context, ChatActivity.class);

        return intent;
    }

    @Override
    public Observable<Collection<ItemModel>> search(@NotNull String query, long limit) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withQuery(query.replaceAll("(\\s\\s\\s*)|(\\n)", " "))
                        .withLimit(limit)
                        .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

}
