package com.ua.erent.module.core.item.domain;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ua.erent.module.core.item.domain.api.ItemProvider;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.item.sync.ItemSynchronizeable;
import com.ua.erent.module.core.networking.util.IApiFilter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 11/28/2016.
 */

public final class ItemDomain implements IItemDomain {

    private final ItemProvider provider;
    private final PublishSubject<Collection<Item>> addedItemsObs;

    @Inject
    public ItemDomain(Application application, ItemProvider provider) {

        this.provider = provider;
        this.addedItemsObs = PublishSubject.create();

        application.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                final Collection<Item> items = intent
                        .getParcelableArrayListExtra(ItemSynchronizeable.ARG_RESULT);

                if(items != null && items.isEmpty()) {
                    addedItemsObs.onNext(items);
                }
            }
        }, new IntentFilter(ItemSynchronizeable.FILTER), null, null);
    }

    @Override
    public Observable<Collection<Item>> fetchItems(@NotNull IApiFilter filter) {
        return provider.fetchItems(filter).map(items -> {
            synchronized (addedItemsObs) {
                addedItemsObs.onNext(items);
            }
            return items;
        });
    }

    @Override
    public Observable<Item> fetchById(@NotNull ItemID id) {
        return provider.fetchItem(id);
    }

    @Override
    public Observable<Collection<Item>> getOnItemsAddedObs() {
        return addedItemsObs.observeOn(AndroidSchedulers.mainThread());
    }
}
