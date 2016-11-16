package com.ua.erent.module.core.item.domain.di;

import com.ua.erent.module.core.item.domain.CategoryAppService;
import com.ua.erent.module.core.item.domain.CategoryDomain;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.ICategoryDomain;
import com.ua.erent.module.core.item.domain.api.CategoriesProvider;
import com.ua.erent.module.core.item.domain.api.ICategoriesProvider;
import com.ua.erent.module.core.item.domain.storage.CategoryStorage;
import com.ua.erent.module.core.item.domain.storage.ICategoriesStorage;
import com.ua.erent.module.core.storage.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Максим on 11/13/2016.
 */
@Module
public final class CategoryModule {

    @Singleton
    @Provides
    ICategoriesStorage provideStorage(DatabaseHelper helper) {
        return new CategoryStorage(helper);
    }

    @Singleton
    @Provides
    ICategoryAppService provideAppService(ICategoriesStorage storage, ICategoryDomain domain) {
        return new CategoryAppService(storage, domain);
    }

    @Provides
    @Singleton
    ICategoriesProvider provideProvider(Retrofit retrofit) {
        return new CategoriesProvider(retrofit);
    }

    @Singleton
    @Provides
    ICategoryDomain provideDomain(ICategoriesStorage storage, ICategoriesProvider provider) {
        return new CategoryDomain(storage, provider);
    }

}
