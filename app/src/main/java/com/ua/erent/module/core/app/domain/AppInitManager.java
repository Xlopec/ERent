package com.ua.erent.module.core.app.domain;

import android.app.Application;

import com.ua.erent.module.core.app.domain.interfaces.IAppInitManager;
import com.ua.erent.module.core.sync.IAppSyncService;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by Максим on 10/30/2016.
 */

public final class AppInitManager implements IAppInitManager {

    private final Application application;
    private final IAppSyncService appSyncService;

    @Inject
    public AppInitManager(Application application, IAppSyncService appSyncService) {
        this.application = application;
        this.appSyncService = appSyncService;
    }

    @Override
    public void initialize() {

        final Thread cleanerTh = new Thread(() -> {

            final File cacheDir = application.getExternalCacheDir();

            if (cacheDir != null) {
                cacheDir.delete();
            }

            application.getCacheDir().delete();
        }, "Cache cleaner thread#");

        cleanerTh.setDaemon(true);
        cleanerTh.setPriority(Thread.MIN_PRIORITY);
        cleanerTh.start();

        appSyncService.start();
    }
}
