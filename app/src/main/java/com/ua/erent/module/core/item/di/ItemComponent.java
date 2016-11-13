package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.sync.Synchronizeable;

import dagger.Component;

/**
 * Created by Максим on 11/13/2016.
 */
@Component(dependencies = AppComponent.class, modules = ItemModule.class)
@ServiceScope
public interface ItemComponent {

    Synchronizeable getSynchronizeable();

}
