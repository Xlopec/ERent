package com.ua.erent.module.core.account.auth.component;

import com.ua.erent.module.core.account.auth.domain.IAuthDomain;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.api.ISessionProvider;
import com.ua.erent.module.core.account.auth.domain.session.ISessionManager;
import com.ua.erent.module.core.account.auth.domain.session.ISessionStorage;
import com.ua.erent.module.core.account.auth.module.AuthModule;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/15/2016.
 */
@Singleton
@Component(modules = AuthModule.class)
public interface AuthComponent {

    IPacketInterceptService getInterceptService();

    ISessionStorage getSessionStorage();

    ISessionManager getSessionManager();

    ISessionProvider getSessionProvider();

    IAuthDomain getAuthDomain();

    IAuthAppService getAuthHandler();

}
