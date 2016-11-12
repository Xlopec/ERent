package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.presenter.MainPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IMainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/11/2016.
 */
@Module
public final class MainModule {

    @Provides
    @ActivityScope
    IMainPresenter provideTestPresenter(IAuthAppService authAppService) {
        return new MainPresenter(authAppService);
    }

}
