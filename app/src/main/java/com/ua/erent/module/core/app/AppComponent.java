package com.ua.erent.module.core.app;

import android.app.Application;
import android.content.Context;

import com.ua.erent.module.core.account.auth.di.AuthModule;
import com.ua.erent.module.core.account.auth.di.UserModule;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.user.domain.IUserAppService;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.init.InitModule;
import com.ua.erent.module.core.init.domain.IInitAppService;
import com.ua.erent.module.core.item.di.ItemModule;
import com.ua.erent.module.core.networking.module.BaseNetworkingModule;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.util.Initializeable;

import java.util.Collection;

import javax.inject.Singleton;

import dagger.Component;

/**
 * <p>
 * Manages application services whose lifetime is bound
 * to Android application
 * </p>
 * Created by Максим on 10/11/2016.
 */
@Singleton
@Component(modules = {
        AppModule.class, AuthModule.class, UserModule.class, NetworkingModule.class,
        BaseNetworkingModule.class, InitModule.class, ItemModule.class
})
public abstract class AppComponent {

    public abstract Context getApplicationContext();

    public abstract Application getApplication();

    public abstract IAppLifecycleManager getAppLifecycleManager();

    public abstract IAuthAppService getAuthAppService();

    public abstract IUserAppService getUserAppService();

    abstract Collection<Initializeable> getInitTargets();

    abstract IInitAppService getInitService();

}
