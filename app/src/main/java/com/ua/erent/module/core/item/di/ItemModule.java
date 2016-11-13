package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.item.sync.ItemDispatcher;
import com.ua.erent.module.core.item.sync.ItemDispatcherImp;
import com.ua.erent.module.core.item.sync.ItemSynchronizeable;
import com.ua.erent.module.core.item.sync.api.ItemProvider;
import com.ua.erent.module.core.item.sync.api.ItemProviderImp;
import com.ua.erent.module.core.sync.Synchronizeable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

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

    @Provides
    @ServiceScope
    ItemProvider provideProvider(Retrofit retrofit) {
        return new ItemProviderImp(retrofit);
    }

    @Provides
    @ServiceScope
    Synchronizeable provideSynchronizeable(ItemProvider itemProvider) {
        return new ItemSynchronizeable(itemProvider);
    }

}
