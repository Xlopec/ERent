package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.content.Intent;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.api.filter.FilterBuilder;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IMyItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.util.ItemConverter;
import com.ua.erent.module.core.presentation.mvp.view.ItemCreateActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 12/11/2016.
 */

public final class MyItemsModel implements IMyItemsModel {

    private final Context context;
    private final IItemAppService itemAppService;
    private final IAuthAppService authAppService;

    public MyItemsModel(Context context, IItemAppService itemAppService, IAuthAppService authAppService) {
        this.context = context;
        this.itemAppService = itemAppService;
        this.authAppService = authAppService;
    }

    @Override
    public Observable<Collection<ItemModel>> fetch(long limit) {
        return itemAppService.fetchItems(new FilterBuilder()
                .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                .sort(FilterBuilder.SortType.ASC)
                .withOwner(authAppService.getSession().getUserId().getId())
                .withLimit(limit)
                .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Observable<Collection<ItemModel>> fetchNext(long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withOwner(authAppService.getSession().getUserId().getId())
                        .withLastIdGreater(lastId)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .sort(FilterBuilder.SortType.ASC)
                        .withLimit(limit)
                        .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @Override
    public Observable<Collection<ItemModel>> fetchPrev(long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withOwner(authAppService.getSession().getUserId().getId())
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .withLastIdLower(lastId)
                        .withLimit(limit)
                        .build())
                .map(data -> ItemConverter.toCategoryModel(context, data));
    }

    @NotNull
    @Override
    public Intent createNewItemIntent() {
        return new Intent(context, ItemCreateActivity.class);
    }
}
