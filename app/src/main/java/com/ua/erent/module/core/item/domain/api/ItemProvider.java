package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemID;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/7/2016.
 */

public interface ItemProvider {

    Observable<Collection<Item>> fetchItems();

    Observable<Item> fetchItem(@NotNull ItemID id);

}
