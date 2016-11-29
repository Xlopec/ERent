package com.ua.erent.module.core.item.di;

import android.app.Application;

import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.IItemDomain;
import com.ua.erent.module.core.item.domain.ItemAppService;
import com.ua.erent.module.core.item.domain.ItemDomain;
import com.ua.erent.module.core.item.domain.api.ItemProvider;
import com.ua.erent.module.core.item.domain.api.ItemProviderImp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Максим on 11/13/2016.
 */
@Module
public final class ItemModule {

    @Singleton
    @Provides
    IItemAppService provideAppService(IItemDomain domain) {
        return new ItemAppService(domain);
    }

    @Provides
    @Singleton
    ItemProvider provideProvider(Retrofit retrofit) {
        return new ItemProviderImp(retrofit);
    }

    @Singleton
    @Provides
    IItemDomain provideDomain(Application app, ItemProvider provider) {
        return new ItemDomain(app, provider);
    }

}
