package com.ua.erent.module.core.item.sync;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.util.Log;

import com.ua.erent.module.core.item.domain.api.ItemProvider;
import com.ua.erent.module.core.sync.Synchronizeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/8/2016.
 */

public final class ItemSynchronizeable implements Synchronizeable {

    private static final String TAG = ItemSynchronizeable.class.getSimpleName();
    /**
     * Filter to register broadcast receiver
     */
    public static final String FILTER = TAG.concat(".filter");
    /**
     * Key to get sync result from bundle
     */
    public static final String ARG_RESULT = TAG.concat(".sync.result");

    private final ItemProvider provider;

    @Inject
    public ItemSynchronizeable(@NotNull ItemProvider provider) {
        this.provider = Preconditions.checkNotNull(provider);
    }

    @Override
    public void synchronize(@NotNull Account account, String token, @NotNull Context context,
                            @NotNull SyncResult syncResult) {

        provider.fetchItems().subscribe(result -> {
            // notify receivers
            final Intent intent = new Intent(ItemSynchronizeable.FILTER);

            intent.putParcelableArrayListExtra(ItemSynchronizeable.ARG_RESULT, new ArrayList<>(result));
            context.sendBroadcast(intent);
        }, th -> Log.w(TAG, String.format("Error occurred while perform sync in %s",
                getClass().getSimpleName()), th)
        );
    }
}
