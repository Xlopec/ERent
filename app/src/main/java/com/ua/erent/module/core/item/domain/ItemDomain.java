package com.ua.erent.module.core.item.domain;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.item.domain.api.IBrandsProvider;
import com.ua.erent.module.core.item.domain.api.IRegionsProvider;
import com.ua.erent.module.core.item.domain.api.ItemProvider;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.Brand;
import com.ua.erent.module.core.item.domain.vo.ItemForm;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.item.domain.vo.Region;
import com.ua.erent.module.core.item.sync.ItemSynchronizeable;
import com.ua.erent.module.core.networking.util.IApiFilter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 11/28/2016.
 */

public final class ItemDomain implements IItemDomain {

    private final IAuthAppService authAppService;
    private final ItemProvider itemProvider;
    private final IBrandsProvider brandProvider;
    private final IRegionsProvider regionsProvider;
    private final PublishSubject<Collection<Item>> addedItemsObs;

    @Inject
    public ItemDomain(Application application, IAuthAppService authAppService, ItemProvider itemProvider,
                      IBrandsProvider brandProvider,
                      IRegionsProvider regionsProvider) {
        this.authAppService = authAppService;

        this.itemProvider = itemProvider;
        this.brandProvider = brandProvider;
        this.regionsProvider = regionsProvider;
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
        return itemProvider.fetchItems(filter).map(items -> {
            synchronized (addedItemsObs) {
                addedItemsObs.onNext(items);
            }
            return items;
        });
    }

    @Override
    public Observable<Item> fetchById(@NotNull ItemID id) {
        return itemProvider.fetchItem(id);
    }

    @Override
    public Observable<Collection<Item>> getOnItemsAddedObs() {
        return addedItemsObs.observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Collection<Brand>> fetchBrands() {
        return brandProvider.fetchBrands();
    }

    @Override
    public Observable<Collection<Region>> fetchRegions() {
        return regionsProvider.fetchRegions();
    }

    @Override
    public Observable<Item> createItem(@NotNull ItemForm form) {

        Preconditions.checkNotNull(form, "creation form == null");

        if (!authAppService.isSessionAlive())
            throw new IllegalStateException(String.format("%s session expired", getClass()));

        return itemProvider.createItem(authAppService.getSession(), form);
    }
}
