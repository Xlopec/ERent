package com.ua.erent.module.core.sync;

import android.accounts.Account;
import android.content.Context;
import android.content.SyncResult;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Implementation of this interface should
 * provide their own synchronization logic
 * </p>
 * Created by Максим on 11/8/2016.
 */

public interface Synchronizeable {

    /**
     * Starts sync process; this process runs in a separate thread than app
     *
     * @param account    session to use to retrieve data from server
     * @param token      token to use for api request
     * @param context    app context
     * @param syncResult sync result to notify sync adapter about changes
     */
    void synchronize(@NotNull Account account, String token, @NotNull Context context, @NotNull SyncResult syncResult);

}