package com.ua.erent.module.core.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.ua.erent.module.core.app.Constant;

import java.util.Collection;

import dagger.internal.Preconditions;

/**
 * <p>
 *     Adapter which handles items synchronization
 * </p>
 * Created by Максим on 11/7/2016.
 */

public final class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    private final Collection<Synchronizeable> synchronizeables;

    public SyncAdapter(Context context, boolean autoInitialize, Collection<Synchronizeable> synchronizeables) {
        super(context, autoInitialize);
        this.synchronizeables = Preconditions.checkNotNull(synchronizeables);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs, Collection<Synchronizeable> synchronizeables) {
        super(context, autoInitialize, allowParallelSyncs);
        this.synchronizeables = Preconditions.checkNotNull(synchronizeables);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        final AccountManager accountManager = AccountManager.get(getContext());
        final String token = accountManager.peekAuthToken(account, Constant.ACCOUNT_TOKEN_TYPE);

        for(final Synchronizeable synchronizeable : synchronizeables) {
            try {
                synchronizeable.synchronize(account, token, getContext(), syncResult);
            } catch (final Exception e) {
                Log.e(TAG, String.format("Failed to perform sync on component %s",
                        synchronizeable.getClass().getName()), e);
            }
        }
        Log.d(TAG, syncResult.toDebugString());
    }
}
