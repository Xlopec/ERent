package com.ua.erent.module.core.di.module;

import android.app.Application;

import com.ua.erent.ITestService;
import com.ua.erent.TestService;
import com.ua.erent.module.core.di.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/11/2016.
 */
@Module
public final class TestModule {

    @Provides
    @PerActivity
    ITestService provideTestService(Application app) {
        return new TestService(app);
    }

}
