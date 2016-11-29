package com.ua.erent.module.core.sync.di;

import android.app.Application;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.sync.AppSyncService;
import com.ua.erent.module.core.sync.IAppSyncService;
import com.ua.erent.module.core.sync.Synchronizeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 11/13/2016.
 */
@Module
public final class AppSyncModule {

    private final Collection<Synchronizeable> synchronizeables;
    private final Application application;

    public AppSyncModule(@NotNull Collection<Synchronizeable> synchronizeables, @NotNull Application application) {
        this.synchronizeables = new ArrayList<>(synchronizeables);
        this.application = application;
    }

    @Provides
    @Singleton
    IAppSyncService provideAppSyncService() {
        return new AppSyncService(application);
    }

    @Provides
    @ServiceScope
    Collection<Synchronizeable> provideSyncTarget() {
        return synchronizeables;
    }

}
