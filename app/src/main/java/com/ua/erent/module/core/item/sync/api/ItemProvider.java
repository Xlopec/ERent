package com.ua.erent.module.core.item.sync.api;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.item.domain.bo.Item;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/7/2016.
 */

public interface ItemProvider {

    Observable<Collection<Item>> fetchItems(@NotNull Session session);

}
