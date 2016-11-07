package com.ua.erent.module.core.item.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * <p>
 *     Adapter which handles items synchronization
 * </p>
 * Created by Максим on 11/7/2016.
 */

public final class ItemSyncAdapter extends AbstractThreadedSyncAdapter {

   /* @Inject
    protected IAuthAppService authAppService;
    @Inject
    protected ItemProvider itemProvider;
    @Inject
    protected ItemDispatcher dispatcher;*/

    public ItemSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public ItemSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        getContext().sendBroadcast(new Intent());
        /*if(authAppService.isSessionAlive()) {

            final Session session = authAppService.getSession();

            itemProvider.fetchItems(session)
                    .subscribe(items -> dispatcher.dispatch(items), th -> dispatcher.dispatchError(th));
        }*/
    }
}
