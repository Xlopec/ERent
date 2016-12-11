package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import android.content.Intent;

import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 12/11/2016.
 */

public interface IMyItemsModel {

    Observable<Collection<ItemModel>> fetch(long limit);

    Observable<Collection<ItemModel>> fetchNext(long limit, long lastId);

    Observable<Collection<ItemModel>> fetchPrev(long limit, long lastId);

    @NotNull
    Intent createNewItemIntent();
}
