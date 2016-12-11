package com.ua.erent.module.core.item.di;

import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.IItemDomain;
import com.ua.erent.module.core.item.domain.ItemAppService;
import com.ua.erent.module.core.item.domain.ItemDomain;
import com.ua.erent.module.core.item.domain.api.BrandsProvider;
import com.ua.erent.module.core.item.domain.api.IBrandsProvider;
import com.ua.erent.module.core.item.domain.api.IRegionsProvider;
import com.ua.erent.module.core.item.domain.api.ItemProvider;
import com.ua.erent.module.core.item.domain.api.ItemProviderImp;
import com.ua.erent.module.core.item.domain.api.RegionsProvider;

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
    ItemProvider provideItemProvider(Retrofit retrofit) {
        return new ItemProviderImp(retrofit);
    }

    @Provides
    @Singleton
    IBrandsProvider provideBrandsProvider(Retrofit retrofit) {
        return new BrandsProvider(retrofit);
    }

    @Provides
    @Singleton
    IRegionsProvider provideRegionsProvider(Retrofit retrofit) {
        return new RegionsProvider(retrofit);
    }

    @Singleton
    @Provides
    IItemDomain provideDomain(Application app, IAuthAppService authAppService,
                              ItemProvider itemProvider, IBrandsProvider brandProvider,
                              IRegionsProvider regionsProvider) {
        return new ItemDomain(app, authAppService, itemProvider, brandProvider, regionsProvider);
    }

}
