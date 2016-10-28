package com.ua.erent.module.core.app.di;

import android.app.Application;

import com.ua.erent.module.core.app.domain.AppService;
import com.ua.erent.module.core.app.domain.AppLifecycleManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppService;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.trash.SomeAppServiceImp;
import com.ua.erent.trash.ISomeAppService;

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
    ISomeAppService provideSomeAppService(IAppLifecycleManager lifecycleManager) {
        return new SomeAppServiceImp(app, lifecycleManager);
    }

    @Provides
    @Singleton
    IAppService provideAppService(IAppLifecycleManager lifecycleManager) {
        return new AppService(lifecycleManager);
    }

    @Provides
    @Singleton
    IAppLifecycleManager provideAppLifecycleManager() {
        return new AppLifecycleManager(app);
    }

}
