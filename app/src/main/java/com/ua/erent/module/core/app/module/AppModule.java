package com.ua.erent.module.core.app.module;

import android.app.Application;

import com.ua.erent.trash.AppServiceImp;
import com.ua.erent.trash.IAppService;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * <p>
 *     Module which provides application context
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
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    IAppService provideAppService() {
        return new AppServiceImp(app);
    }

}
