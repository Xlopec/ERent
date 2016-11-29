package com.ua.erent.module.core.app;

import android.app.Application;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.ua.erent.module.core.app.domain.AppInitManager;
import com.ua.erent.module.core.app.domain.AppLifecycleManager;
import com.ua.erent.module.core.app.domain.AppService;
import com.ua.erent.module.core.app.domain.interfaces.IAppInitManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppService;
import com.ua.erent.module.core.networking.util.ConnectionManager;
import com.ua.erent.module.core.storage.DatabaseHelper;
import com.ua.erent.module.core.sync.IAppSyncService;
import com.ua.erent.module.core.sync.di.AppSyncModule;

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
@Module(includes = AppSyncModule.class)
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
    Context provideAppContext() {
        return app;
    }

    @Provides
    @Singleton
    IAppService provideAppService(IAppLifecycleManager lifecycleManager, IAppInitManager manager) {
        return new AppService(lifecycleManager, manager);
    }

    @Provides
    @Singleton
    ConnectionManager provideConnectionManager(Application application) {
        return new ConnectionManager(application);
    }

    @Provides
    @Singleton
    IAppLifecycleManager provideAppLifecycleManager() {
        return new AppLifecycleManager(app);
    }

    @Provides
    @Singleton
    IAppInitManager provideAppInitManager(IAppSyncService appSyncService) {
        return new AppInitManager(app, appSyncService);
    }

    @Provides
    @Singleton
    DatabaseHelper provideDbHelper() {
        return OpenHelperManager.getHelper(app, DatabaseHelper.class);
    }

}
