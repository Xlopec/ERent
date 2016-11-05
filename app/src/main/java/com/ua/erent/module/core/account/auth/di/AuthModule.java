package com.ua.erent.module.core.account.auth.di;

import android.app.Activity;
import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.AuthAppService;
import com.ua.erent.module.core.account.auth.domain.AuthDomain;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.IAuthDomain;
import com.ua.erent.module.core.account.auth.domain.api.auth.AuthProvider;
import com.ua.erent.module.core.account.auth.domain.api.auth.IAuthProvider;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.domain.session.storage.SessionStorage;
import com.ua.erent.module.core.init.domain.IInitAppService;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.storage.DatabaseHelper;
import com.ua.erent.module.core.storage.ISingleItemStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.internal.Preconditions;
import retrofit2.Retrofit;

/**
 * Created by Максим on 10/15/2016.
 */
@Module
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
    ISingleItemStorage<Session> provideSessionStorage(Application app, DatabaseHelper helper) {
        return new SessionStorage(app, helper);
    }

    @Provides
    @Singleton
    IAuthProvider provideAuthProvider(Retrofit retrofit) {
        return new AuthProvider(retrofit);
    }

    @Provides
    @Singleton
    IAuthDomain provideAuthDomain(Class<? extends Activity> loginActivity, Application app,
                                  ISingleItemStorage<Session> sessionStorage,
                                  IAuthProvider provider, IInitAppService initAppService) {
        return new AuthDomain(loginActivity, app, sessionStorage, provider, initAppService);
    }

    @Provides
    @Singleton
    IAuthAppService provideAuthHandler(IPacketInterceptService interceptService,
                                       ISingleItemStorage<Session> storage,
                                       IAuthDomain domain) {
        return new AuthAppService(interceptService, storage, domain);
    }

}
