package com.ua.erent.module.core.item.domain.di;

import com.ua.erent.module.core.item.domain.CategoryAppService;
import com.ua.erent.module.core.item.domain.CategoryDomain;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.ICategoryDomain;
import com.ua.erent.module.core.item.domain.storage.CategoryStorage;
import com.ua.erent.module.core.item.domain.storage.ICategoriesStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 11/13/2016.
 */
@Module
public final class CategoryModule {

    @Singleton
    @Provides
    ICategoriesStorage provideStorage() {
        return new CategoryStorage();
    }

    @Singleton
    @Provides
    ICategoryAppService provideAppService(ICategoriesStorage storage) {
        return new CategoryAppService(storage);
    }

    @Singleton
    @Provides
    ICategoryDomain provideDomain(ICategoriesStorage storage) {
        return new CategoryDomain(storage);
    }

}
