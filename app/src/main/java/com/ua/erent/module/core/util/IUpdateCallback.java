package com.ua.erent.module.core.util;

import android.support.annotation.MainThread;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 *     This class defines signature for a simple
 *     update callback.
 * </p>
 * Created by Максим on 09.05.2016.
 */
public abstract class IUpdateCallback {
    /**
     * This method is called before execution.
     * It should be used mainly to prepare
     * user interface to operation execution
     * */
    @MainThread
    public void onPreExecute() {}

    /**
     * This method is called after execution.
     * This method returns result of execution which
     * is encoded in {@code resultCode}
     * */
    @MainThread
    public abstract void onResult();

    /**
     * This method is called if exception occurs while
     * executing callback
     * */
    @MainThread
    public abstract void onException(@NotNull  Exception exc);

    /**
     * This method should be called when
     * process was canceled
     * */
    @MainThread
    public void onCancel() {}

}
