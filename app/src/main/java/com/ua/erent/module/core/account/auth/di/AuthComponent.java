package com.ua.erent.module.core.account.auth.di;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.app.di.AppModule;
import com.ua.erent.module.core.networking.module.NetworkingModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/15/2016.
 */
@Singleton
@Component(modules = {AuthModule.class, AppModule.class, NetworkingModule.class})
public interface AuthComponent {

    IAuthAppService getAuthAppService();

}
