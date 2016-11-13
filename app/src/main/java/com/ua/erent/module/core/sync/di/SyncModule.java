package com.ua.erent.module.core.sync.di;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.sync.Synchronizeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 11/13/2016.
 */
@Module
public final class SyncModule {

    private final Collection<Synchronizeable> synchronizeables;

    public SyncModule(@NotNull Collection<Synchronizeable> synchronizeables) {
        this.synchronizeables = new ArrayList<>(synchronizeables);
    }

    @Provides
    @ServiceScope
    Collection<Synchronizeable> provideSyncTarget() {
        return synchronizeables;
    }

}
