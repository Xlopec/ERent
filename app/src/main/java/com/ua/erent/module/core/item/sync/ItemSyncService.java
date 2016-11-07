package com.ua.erent.module.core.item.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.item.di.SyncServiceComponent;

public final class ItemSyncService extends Service {

    private static ItemSyncAdapter syncAdapter = null;
    private static final Object adapterLock = new Object();

    public ItemSyncService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (adapterLock) {

            Injector injector = Injector.injector();

            if(syncAdapter == null) {
                final SyncServiceComponent syncServiceComponent =
                        injector.getComponent(getApplicationContext(), SyncServiceComponent.class);
                syncAdapter = new ItemSyncAdapter(getApplicationContext(), true);
            }
        }
    }
}
