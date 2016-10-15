package com.ua.erent.module.core.account.auth.component;

import com.ua.erent.module.core.account.auth.domain.IAuthDomain;
import com.ua.erent.module.core.account.auth.domain.IAuthHandler;
import com.ua.erent.module.core.account.auth.domain.api.ISessionProvider;
import com.ua.erent.module.core.account.auth.domain.session.ISessionManager;
import com.ua.erent.module.core.account.auth.domain.session.ISessionStorage;
import com.ua.erent.module.core.account.auth.module.AuthModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/15/2016.
 */
@Singleton
@Component(modules = AuthModule.class)
public interface AuthComponent {

    ISessionStorage getSessionStorage();

    ISessionManager getSessionManager();

    ISessionProvider getSessionProvider();

    IAuthDomain getAuthDomain();

    IAuthHandler getAuthHandler();

}
