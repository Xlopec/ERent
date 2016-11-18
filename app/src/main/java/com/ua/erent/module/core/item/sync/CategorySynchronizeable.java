package com.ua.erent.module.core.item.sync;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.util.Log;

import com.ua.erent.module.core.item.domain.api.ICategoriesProvider;
import com.ua.erent.module.core.sync.Synchronizeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Максим on 11/16/2016.
 */

public final class CategorySynchronizeable implements Synchronizeable {

    private static final String TAG = CategorySynchronizeable.class.getSimpleName();
    /**
     * Filter to register broadcast receiver
     */
    public static final String FILTER = TAG.concat(".filter");
    /**
     * Key to get sync result from bundle
     */
    public static final String ARG_RESULT = TAG.concat(".sync.result");

    private final ICategoriesProvider provider;

    @Inject
    public CategorySynchronizeable(ICategoriesProvider provider) {
        this.provider = provider;
    }

    @Override
    public void synchronize(@NotNull Account account, String token, @NotNull Context context, @NotNull SyncResult syncResult) {

        provider.fetchCategories().subscribe(categories -> {
                    // notify receivers
                    final Intent intent = new Intent(CategorySynchronizeable.FILTER);

                    intent.putParcelableArrayListExtra(CategorySynchronizeable.ARG_RESULT, new ArrayList<>(categories));
                    context.sendBroadcast(intent);

                }, th -> Log.w(TAG, String.format("Error occurred while perform sync in %s", getClass().getSimpleName()), th)
        );
    }
}
