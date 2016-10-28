package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.model.IPreLoaderModel;
import com.ua.erent.module.core.presentation.mvp.model.PreLoaderModel;
import com.ua.erent.module.core.presentation.mvp.presenter.IPreLoadPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.PreLoaderPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/27/2016.
 */
@Module
public final class PreLoaderModule {

    @Provides
    @ActivityScope
    IPreLoaderModel providePreLoaderModel() {
        return new PreLoaderModel();
    }

    @Provides
    @ActivityScope
    IPreLoadPresenter provideLoginPresenter(IPreLoaderModel model) {
        return new PreLoaderPresenter(model);
    }

}
