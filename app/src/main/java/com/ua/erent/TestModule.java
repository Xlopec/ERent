package com.ua.erent;

import android.app.Application;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/9/2016.
 */
@Module
public class TestModule {

    private final Application app;

    public TestModule(@NotNull Application application) {
        app = application;
    }

    @Provides
    @Singleton
    protected Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    ITestService provideTestService() {
        return new TestService(app);
    }

}
