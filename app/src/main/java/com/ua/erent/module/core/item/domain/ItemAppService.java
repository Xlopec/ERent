package com.ua.erent.module.core.item.domain;

import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.networking.util.IApiFilter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 11/28/2016.
 */

public final class ItemAppService implements IItemAppService {

    private final IItemDomain domain;

    @Inject
    public ItemAppService(IItemDomain domain) {
        this.domain = domain;
    }

    @Override
    public Observable<Collection<Item>> fetchItems(@NotNull IApiFilter filter) {
        return domain.fetchItems(filter);
    }

    @Override
    public Observable<Item> fetchById(@NotNull ItemID id) {
        return domain.fetchById(id);
    }

    @Override
    public Observable<Collection<Item>> getOnItemsAddedObs() {
        return domain.getOnItemsAddedObs();
    }
}
