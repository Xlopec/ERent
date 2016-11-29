package com.ua.erent.module.core.item.domain;

import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.networking.util.IApiFilter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/27/2016.
 */

public interface IItemAppService {

    Observable<Collection<Item>> fetchItems(@NotNull IApiFilter filter);

    Observable<Item> fetchById(@NotNull ItemID id);

    Observable<Collection<Item>> getOnItemsAddedObs();

}
