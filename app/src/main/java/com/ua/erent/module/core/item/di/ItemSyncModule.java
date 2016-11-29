package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.item.domain.api.CategoriesProvider;
import com.ua.erent.module.core.item.domain.api.ItemProviderImp;
import com.ua.erent.module.core.item.sync.CategorySynchronizeable;
import com.ua.erent.module.core.item.sync.ItemSynchronizeable;
import com.ua.erent.module.core.sync.Synchronizeable;

import java.util.Arrays;
import java.util.Collection;

import dagger.Module;
import dagger.Provides;
import dagger.internal.Preconditions;
import retrofit2.Retrofit;

/**
 * Created by Максим on 11/7/2016.
 */
@Module
public final class ItemSyncModule {

    private final Retrofit retrofit;

    public ItemSyncModule(Retrofit retrofit) {
        this.retrofit = Preconditions.checkNotNull(retrofit);
    }

    @Provides
    @ServiceScope
    Collection<Synchronizeable> provideSynchronizeable() {
        return Arrays.asList(
                new CategorySynchronizeable(new CategoriesProvider(retrofit)),
                new ItemSynchronizeable(new ItemProviderImp(retrofit))
        );
    }

}
