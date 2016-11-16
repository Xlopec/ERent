package com.ua.erent.module.core.item.sync;

import android.accounts.Account;
import android.content.Context;
import android.content.SyncResult;

import com.ua.erent.module.core.sync.Synchronizeable;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/16/2016.
 */

public final class CategorySynchronizeable implements Synchronizeable {



    @Override
    public void synchronize(@NotNull Account account, String token, @NotNull Context context, @NotNull SyncResult syncResult) {

    }
}
