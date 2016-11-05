package com.ua.erent.module.core.app.di;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.ua.erent.module.core.storage.DatabaseHelper;
import com.ua.erent.module.core.app.domain.AppInitManager;
import com.ua.erent.module.core.app.domain.AppLifecycleManager;
import com.ua.erent.module.core.app.domain.AppService;
import com.ua.erent.module.core.app.domain.interfaces.IAppInitManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppService;
import com.ua.erent.trash.ISomeAppService;
import com.ua.erent.trash.SomeAppServiceImp;

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

    private static final String TAG = AppModule.class.getSimpleName();

    private final Application app;
  //  private final DatabaseHelper databaseHelper;

    public AppModule(@NotNull Application application) {
        app = application;
   //     databaseHelper = OpenHelperManager.getHelper(application, DatabaseHelper.class);
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
    IAppService provideAppService(IAppLifecycleManager lifecycleManager, IAppInitManager manager) {
        return new AppService(lifecycleManager, manager);
    }

    @Provides
    @Singleton
    IAppLifecycleManager provideAppLifecycleManager() {
        return new AppLifecycleManager(app);
    }

    @Provides
    @Singleton
    IAppInitManager provideAppInitManager() {
        return new AppInitManager(app);
    }

    @Provides
    @Singleton
    DatabaseHelper provideDbHelper() {
        return OpenHelperManager.getHelper(app, DatabaseHelper.class);
    }

}
