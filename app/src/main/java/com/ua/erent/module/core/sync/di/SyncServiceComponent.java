package com.ua.erent.module.core.sync.di;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.di.target.InjectableServiceComponent;
import com.ua.erent.module.core.sync.SyncService;
import com.ua.erent.module.core.sync.Synchronizeable;

import java.util.Collection;

import dagger.Component;

/**
 * Created by Максим on 11/7/2016.
 */
@ServiceScope
@Component(modules = AppSyncModule.class)
public interface SyncServiceComponent extends InjectableServiceComponent<SyncService> {

    Collection<Synchronizeable> getSyncTarget();

}
