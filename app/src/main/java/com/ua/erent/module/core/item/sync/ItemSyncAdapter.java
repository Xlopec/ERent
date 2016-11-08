package com.ua.erent.module.core.item.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.ua.erent.module.core.item.sync.api.ItemProvider;

/**
 * <p>
 *     Adapter which handles items synchronization
 * </p>
 * Created by Максим on 11/7/2016.
 */

public final class ItemSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = ItemSyncAdapter.class.getSimpleName();

   /* @Inject
    protected IAuthAppService authAppService;
    @Inject
    protected ItemProvider itemProvider;
    @Inject
    protected ItemDispatcher dispatcher;*/

    private final ItemProvider provider;

    public ItemSyncAdapter(Context context, boolean autoInitialize, ItemProvider provider) {
        super(context, autoInitialize);
        this.provider = provider;
    }

    public ItemSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs, ItemProvider provider) {
        super(context, autoInitialize, allowParallelSyncs);
        this.provider = provider;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        this.provider.fetchItems().subscribe(result -> {
            final String str = result.toString();
            Log.i("Tag", str);
        }, th -> {
            Log.e("Tag", "error occurred", th);
        });
        getContext().sendBroadcast(new Intent());

        Log.d(TAG, syncResult.toDebugString());
        /*if(authAppService.isSessionAlive()) {

            final Session session = authAppService.getSession();

            itemProvider.fetchItems(session)
                    .subscribe(items -> dispatcher.dispatch(items), th -> dispatcher.dispatchError(th));
        }*/
    }
}
