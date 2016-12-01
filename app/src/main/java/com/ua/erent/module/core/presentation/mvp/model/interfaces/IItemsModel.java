package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/28/2016.
 */

public interface IItemsModel {

    Observable<Collection<ItemModel>> fetch(long limit);

    Observable<Collection<ItemModel>> fetchNext(long limit, long lastId);

    Observable<Collection<ItemModel>> fetchPrev(long limit, long lastId);

    Observable<Collection<ItemModel>> getOnItemAddedObs();

}
