package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.di.target.InjectableServiceComponent;
import com.ua.erent.module.core.item.sync.SyncService;
import com.ua.erent.module.core.item.sync.api.ItemProvider;

import dagger.Component;

/**
 * Created by Максим on 11/7/2016.
 */
@ServiceScope
@Component(modules = SyncModule.class)
public interface SyncServiceComponent extends InjectableServiceComponent<SyncService> {

    ItemProvider getItemProvider();

}
