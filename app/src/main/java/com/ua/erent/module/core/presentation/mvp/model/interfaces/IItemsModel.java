package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import android.content.Intent;

import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/28/2016.
 */

public interface IItemsModel {

    boolean isSessionAlive();

    Observable<Collection<ItemModel>> fetch(long categoryId, long limit);

    Observable<Collection<ItemModel>> fetchNext(long categoryId, long limit, long lastId);

    Observable<Collection<ItemModel>> fetchPrev(long categoryId, long limit, long lastId);

    Observable<Collection<ItemModel>> getOnItemAddedObs();

    Intent createItemDetailsIntent(@NotNull ItemModel item);

    Intent createComplainIntent(@NotNull String email, @NotNull String subject, @NotNull String body);
}
