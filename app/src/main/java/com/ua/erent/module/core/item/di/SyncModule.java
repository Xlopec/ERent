package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.di.scopes.ServiceScope;
import com.ua.erent.module.core.item.sync.api.ItemProvider;
import com.ua.erent.module.core.item.sync.api.ItemProviderImp;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import dagger.Provides;
import dagger.internal.Preconditions;
import retrofit2.Retrofit;

/**
 * Created by Максим on 11/7/2016.
 */
@Module
public final class SyncModule {

    private final Retrofit retrofit;

    public SyncModule(@NotNull Retrofit retrofit) {
        this.retrofit = Preconditions.checkNotNull(retrofit);
    }

    @Provides
    @ServiceScope
    ItemProvider provideProvider() {
        return new ItemProviderImp(retrofit);
    }

}
