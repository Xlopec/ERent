package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.item.sync.ItemSynchronizeable;
import com.ua.erent.module.core.item.sync.api.ItemProvider;
import com.ua.erent.module.core.item.sync.api.ItemProviderImp;
import com.ua.erent.module.core.sync.Synchronizeable;

import retrofit2.Retrofit;

/**
 * Created by Максим on 11/7/2016.
 */
//@Module
public final class ItemSyncModule {

  //  @Provides
  //  @ServiceScope
    ItemProvider provideProvider(Retrofit retrofit) {
        return new ItemProviderImp(retrofit);
    }

  //  @Provides
  //  @ServiceScope
    Synchronizeable provideSynchronizeable(ItemProvider itemProvider) {
        return new ItemSynchronizeable(itemProvider);
    }

}
