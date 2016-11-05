package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.presenter.TestPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ITestPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/11/2016.
 */
@Module
public final class TestModule {

    @Provides
    @ActivityScope
    ITestPresenter provideTestPresenter(IAuthAppService authAppService) {
        return new TestPresenter(authAppService);
    }

}
