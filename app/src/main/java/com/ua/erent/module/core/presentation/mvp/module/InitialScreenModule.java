package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IInitialScreenModel;
import com.ua.erent.module.core.presentation.mvp.model.InitialScreenModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IInitialScreenPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.InitialScreenPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/28/2016.
 */
@Module
public final class InitialScreenModule {

    @Provides
    @ActivityScope
    IInitialScreenModel provideModel() {
        return new InitialScreenModel();
    }

    @Provides
    @ActivityScope
    IInitialScreenPresenter providePresenter(IInitialScreenModel model) {
        return new InitialScreenPresenter(model);
    }

}
