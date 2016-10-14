package com.ua.erent.module.core.util;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/14/2016.
 */

public interface IObserver<T> {

    void onValueChanged(@NotNull Observable<T> obs, T prev, T cur);

}
