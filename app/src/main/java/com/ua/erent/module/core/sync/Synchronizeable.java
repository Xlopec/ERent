package com.ua.erent.module.core.sync;

import android.content.Context;
import android.content.SyncResult;

import com.ua.erent.module.core.account.auth.domain.bo.Session;

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
     * @param session    session to use to retrieve data from server
     * @param syncResult sync result to notify sync adapter about changes
     * @param context    app context
     */
    void synchronize(@NotNull Session session, @NotNull Context context, @NotNull SyncResult syncResult);

}