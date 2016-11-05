package com.ua.erent.module.core.account.auth.di;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.IAuthDomain;
import com.ua.erent.module.core.account.auth.domain.api.auth.IAuthProvider;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.storage.ISingleItemStorage;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/15/2016.
 */
@Singleton
@Component(modules = AuthModule.class)
public interface AuthComponent {

    IPacketInterceptService getInterceptService();

    ISingleItemStorage<Session> getSessionStorage();

    IAuthProvider getSessionProvider();

    IAuthDomain getAuthDomain();

    IAuthAppService getAuthHandler();

}
