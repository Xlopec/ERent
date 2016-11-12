package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.presentation.mvp.model.CategoriesModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.CategoriesPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICategoriesPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 11/12/2016.
 */
@Module
public final class CategoriesModule {

    @ActivityScope
    @Provides
    ICategoriesPresenter providePresenter() {
        return new CategoriesPresenter();
    }

    @ActivityScope
    @Provides
    ICategoriesModel provideModel(ICategoryAppService categoryAppService) {
        return new CategoriesModel(categoryAppService);
    }

}
