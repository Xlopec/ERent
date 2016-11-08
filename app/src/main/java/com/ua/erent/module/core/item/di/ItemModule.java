package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.item.sync.ItemDispatcher;
import com.ua.erent.module.core.item.sync.ItemDispatcherImp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 11/7/2016.
 */
@Module
public final class ItemModule {

    @Provides
    @Singleton
    ItemDispatcher provideDispatcher() {
        return new ItemDispatcherImp();
    }

}
