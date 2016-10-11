package com.ua.erent.module.core.di.module;

import android.app.Application;

import com.ua.erent.AppServiceImp;
import com.ua.erent.IAppService;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * <p>
 *     Module that provides with application
 *     context
 * </p>
 * Created by Максим on 10/9/2016.
 */
@Module
public final class AppModule {

    private final Application app;

    public AppModule(@NotNull Application application) {
        app = application;
    }

    @Provides
    @Singleton
    protected Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    protected IAppService provideAppService() {
        return new AppServiceImp(app);
    }

}
