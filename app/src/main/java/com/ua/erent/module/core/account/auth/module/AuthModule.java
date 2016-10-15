package com.ua.erent.module.core.account.auth.module;

import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.AuthDomain;
import com.ua.erent.module.core.account.auth.domain.AuthHandler;
import com.ua.erent.module.core.account.auth.domain.IAuthDomain;
import com.ua.erent.module.core.account.auth.domain.IAuthHandler;
import com.ua.erent.module.core.account.auth.domain.api.ISessionProvider;
import com.ua.erent.module.core.account.auth.domain.api.SessionProvider;
import com.ua.erent.module.core.account.auth.domain.session.ISessionManager;
import com.ua.erent.module.core.account.auth.domain.session.ISessionStorage;
import com.ua.erent.module.core.account.auth.domain.session.SessionManager;
import com.ua.erent.module.core.account.auth.domain.session.SessionStorage;
import com.ua.erent.module.core.app.module.AppModule;
import com.ua.erent.module.core.networking.module.NetworkingModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Максим on 10/15/2016.
 */
@Module(includes = {AppModule.class, NetworkingModule.class})
public final class AuthModule {

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
    IAuthDomain provideAuthDomain(ISessionManager sessionManager, ISessionProvider provider) {
        return new AuthDomain(sessionManager, provider);
    }

    @Provides
    @Singleton
    IAuthHandler provideAuthHandler(IAuthDomain domain) {
        return new AuthHandler(domain);
    }

}
