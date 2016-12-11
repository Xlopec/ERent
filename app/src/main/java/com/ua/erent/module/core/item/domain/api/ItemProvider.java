package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.item.domain.vo.ItemForm;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.networking.util.IApiFilter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/7/2016.
 */

public interface ItemProvider {

    Observable<Collection<Item>> fetchItems();

    Observable<Collection<Item>> fetchItems(@NotNull IApiFilter filter);

    Observable<Item> fetchItem(@NotNull ItemID id);

    Observable<Item> createItem(@NotNull Session session, @NotNull ItemForm form);

}
