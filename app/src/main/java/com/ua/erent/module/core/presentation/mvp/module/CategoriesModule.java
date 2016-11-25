package com.ua.erent.module.core.presentation.mvp.module;

import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.user.domain.IUserAppService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.networking.util.ConnectionManager;
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
    ICategoriesPresenter providePresenter(ICategoriesModel model) {
        return new CategoriesPresenter(model);
    }

    @ActivityScope
    @Provides
    ICategoriesModel provideModel(Application app, ICategoryAppService categoryAppService,
                                  ConnectionManager connectionManager,
                                  IAuthAppService authAppService, IUserAppService userAppService) {
        return new CategoriesModel(app, categoryAppService, connectionManager, authAppService, userAppService);
    }

}
