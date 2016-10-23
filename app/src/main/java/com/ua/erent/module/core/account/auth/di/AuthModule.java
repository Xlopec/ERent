package com.ua.erent.module.core.account.auth.di;

import android.app.Activity;
import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.AuthAppService;
import com.ua.erent.module.core.account.auth.domain.AuthDomain;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.IAuthDomain;
import com.ua.erent.module.core.account.auth.domain.api.ISessionProvider;
import com.ua.erent.module.core.account.auth.domain.api.SessionProvider;
import com.ua.erent.module.core.account.auth.domain.init.InitializationManager;
import com.ua.erent.module.core.account.auth.domain.session.ISessionManager;
import com.ua.erent.module.core.account.auth.domain.session.ISessionStorage;
import com.ua.erent.module.core.account.auth.domain.session.SessionManager;
import com.ua.erent.module.core.account.auth.domain.session.SessionStorage;
import com.ua.erent.module.core.app.module.AppModule;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.util.Initializeable;

import java.util.Collection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.internal.Preconditions;
import retrofit2.Retrofit;

/**
 * Created by Максим on 10/15/2016.
 */
@Module(includes = {AppModule.class, NetworkingModule.class, InitModule.class})
public final class AuthModule {

    private final Class<? extends Activity> loginActivity;

    public AuthModule(Class<? extends Activity> loginActivity) {
        this.loginActivity = Preconditions.checkNotNull(loginActivity);
    }

    @Provides
    Class<? extends Activity> provideLoginActivity() {
        return loginActivity;
    }

    @Provides
    @Singleton
    ISessionStorage provideSessionStorage(Application app) {
        return new SessionStorage(app);
    }

    @Provides
    @Singleton
    ISessionManager provideSessionManager(ISessionStorage sessionStorage) {
        return new SessionManager(sessionStorage);
    }

    @Provides
    @Singleton
    ISessionProvider provideSessionProvider(Retrofit retrofit) {
        return new SessionProvider(retrofit);
    }

    @Provides
    @Singleton
    IAuthDomain provideAuthDomain(Class<? extends Activity> loginActivity, Application app, ISessionManager sessionManager,
                                  ISessionProvider provider, InitializationManager initializationManager,
                                  Collection<? extends Initializeable> initializeables) {
        return new AuthDomain(loginActivity, app, sessionManager, provider, initializationManager, initializeables);
    }

    @Provides
    @Singleton
    IAuthAppService provideAuthHandler(IPacketInterceptService interceptService, ISessionManager sessionManager,
                                       IAuthDomain domain) {
        return new AuthAppService(interceptService, sessionManager, domain);
    }

}
