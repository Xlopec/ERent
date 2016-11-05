package com.ua.erent.module.core.storage;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/5/2016.
 */

public interface ISingleItemStorage<T> {

    void store(@NotNull T t);

    void clear();

    T getItem();

    boolean hasItem();

}
