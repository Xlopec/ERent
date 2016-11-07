package com.ua.erent.module.core.item.sync;

import com.ua.erent.module.core.item.domain.bo.Item;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/7/2016.
 */

public interface ItemDispatcher {

    void dispatch(@NotNull Collection<Item> items);

    void dispatchError(@NotNull Throwable th);

}
