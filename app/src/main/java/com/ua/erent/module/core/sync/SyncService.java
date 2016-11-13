package com.ua.erent.module.core.sync;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ua.erent.module.core.di.target.InjectableService;
import com.ua.erent.module.core.sync.di.SyncServiceComponent;

import java.util.Collection;

import javax.inject.Inject;

public final class SyncService extends InjectableService<SyncService> {

    private static final String TAG = SyncService.class.getSimpleName();
    private static final Object adapterLock = new Object();
    private static SyncAdapter syncAdapter = null;

    @Inject
    protected Collection<Synchronizeable> synchronizeables;

    public SyncService() {
        super(SyncServiceComponent.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "sync service onCreate#");

        synchronized (adapterLock) {

            if(syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true, synchronizeables);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "sync service onDestroy#");
    }
}
