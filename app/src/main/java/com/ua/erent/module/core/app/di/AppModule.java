package com.ua.erent.module.core.app.di;

import android.app.Application;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ReferenceObjectCache;
import com.ua.erent.module.core.account.auth.domain.api.db.DatabaseHelper;
import com.ua.erent.module.core.account.auth.domain.api.db.SessionDao;
import com.ua.erent.module.core.account.auth.domain.api.db.SessionPO;
import com.ua.erent.module.core.app.domain.AppInitManager;
import com.ua.erent.module.core.app.domain.AppLifecycleManager;
import com.ua.erent.module.core.app.domain.AppService;
import com.ua.erent.module.core.app.domain.interfaces.IAppInitManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppService;
import com.ua.erent.trash.ISomeAppService;
import com.ua.erent.trash.SomeAppServiceImp;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

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
    private final DatabaseHelper databaseHelper;

    public AppModule(@NotNull Application application) {
        app = application;
        databaseHelper = OpenHelperManager.getHelper(application, DatabaseHelper.class);
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
    SessionDao provideSessionDao() {

        try {
            final SessionDao dao =
                    new SessionDao(databaseHelper.getConnectionSource(), SessionPO.class);
            dao.setObjectCache(true);
            dao.setObjectCache(ReferenceObjectCache.makeSoftCache());
            return dao;
        } catch (final SQLException e) {
            Log.e(TAG, "Exception while creating session dao", e);
        }

        throw new RuntimeException("Failed to get session dao!");
    }

}
