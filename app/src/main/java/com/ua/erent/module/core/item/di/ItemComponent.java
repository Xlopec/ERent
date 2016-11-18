package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.sync.Synchronizeable;

import java.util.Collection;

import dagger.Component;

/**
 * Created by Максим on 11/13/2016.
 */
@Component(modules = ItemModule.class)
@ServiceScope
public interface ItemComponent {

    Collection<Synchronizeable> getSynchronizeables();

}
