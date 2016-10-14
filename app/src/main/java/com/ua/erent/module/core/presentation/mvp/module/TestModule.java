package com.ua.erent.module.core.presentation.mvp.module;

import android.app.Application;

import com.ua.erent.trash.ITestService;
import com.ua.erent.module.core.presentation.mvp.presenter.TestPresenter;
import com.ua.erent.trash.TestService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.presenter.ITestPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/11/2016.
 */
@Module
public final class TestModule {

    @Provides
    @ActivityScope
    ITestService provideTestService(Application app) {
        return new TestService(app);
    }

    @Provides
    @ActivityScope
    protected ITestPresenter provideTestPresenter() {
        return new TestPresenter();
    }

}
